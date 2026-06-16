package com.myowntrip.app.data.wallet

import android.content.Context
import android.net.Uri
import java.util.zip.Inflater
import kotlin.math.min

object WalletDocumentContentReader {

  private const val MAX_BYTES = 512_000

  fun readSearchableText(context: Context, uri: Uri, mimeType: String?): String {
    val bytes = context.contentResolver.openInputStream(uri)?.use { stream ->
      val buffer = ByteArray(MAX_BYTES)
      val read = stream.read(buffer)
      if (read <= 0) ByteArray(0) else buffer.copyOf(read)
    } ?: return ""

    return when {
      mimeType?.startsWith("text/") == true -> decodeUtf8(bytes)
      mimeType == "application/pdf" -> extractPdfRoughText(bytes)
      mimeType?.startsWith("image/") == true -> ""
      else -> extractPdfRoughText(bytes)
    }
  }

  private fun decodeUtf8(bytes: ByteArray): String =
    runCatching { String(bytes, Charsets.UTF_8) }.getOrDefault("")

  /**
   * Extrae texto de PDFs sin dependencias externas: cadenas Tj/TJ en claro y streams
   * FlateDecode / ASCII85Decode (p. ej. ReportLab, confirmaciones de reserva).
   */
  internal fun extractPdfRoughText(bytes: ByteArray): String {
    val chunks = buildList {
      add(extractTjStrings(bytes))
      add(extractFromDecodedStreams(bytes))
    }.filter { it.isNotBlank() }

    return chunks.joinToString(" ")
      .replace(Regex("""\s+"""), " ")
      .trim()
  }

  private fun extractFromDecodedStreams(bytes: ByteArray): String {
    val streamToken = "stream".toByteArray(Charsets.US_ASCII)
    val endToken = "endstream".toByteArray(Charsets.US_ASCII)
    val results = mutableListOf<String>()
    var searchFrom = 0

    while (searchFrom < bytes.size) {
      val streamIdx = indexOf(bytes, streamToken, searchFrom)
      if (streamIdx < 0) break

      val headerStart = maxOf(0, streamIdx - 800)
      val header = String(bytes, headerStart, streamIdx - headerStart, Charsets.ISO_8859_1)
      val filters = parseFilters(header)
      if (filters.isEmpty()) {
        searchFrom = streamIdx + streamToken.size
        continue
      }

      var dataStart = streamIdx + streamToken.size
      if (startsWith(bytes, dataStart, "\r\n")) dataStart += 2
      else if (dataStart < bytes.size && bytes[dataStart] == '\n'.code.toByte()) dataStart += 1

      val endIdx = indexOf(bytes, endToken, dataStart)
      if (endIdx < 0) break

      val lengthMatch = Regex("""/Length\s+(\d+)""").find(header)
      val streamData = if (lengthMatch != null) {
        val len = lengthMatch.groupValues[1].toInt()
        bytes.copyOfRange(dataStart, min(dataStart + len, bytes.size))
      } else {
        var dataEnd = endIdx
        if (dataEnd > dataStart && bytes[dataEnd - 1] == '\n'.code.toByte()) dataEnd--
        if (dataEnd > dataStart && bytes[dataEnd - 1] == '\r'.code.toByte()) dataEnd--
        bytes.copyOfRange(dataStart, dataEnd)
      }

      decodeStream(streamData, filters)?.let { decoded ->
        results += extractTjStrings(decoded)
      }

      searchFrom = endIdx + endToken.size
    }

    return results.joinToString(" ")
  }

  private fun parseFilters(header: String): List<String> {
    Regex("""/Filter\s*\[\s*([^\]]+)\]""").find(header)?.let { match ->
      return match.groupValues[1]
        .split('/')
        .map { it.trim() }
        .filter { it.isNotBlank() }
        .map { "/$it" }
    }
    Regex("""/Filter\s*/(\w+)""").find(header)?.groupValues?.getOrNull(1)?.let {
      return listOf("/$it")
    }
    return emptyList()
  }

  private fun decodeStream(data: ByteArray, filters: List<String>): ByteArray? {
    var current = data
    for (filter in filters) {
      current = when (filter) {
        "/ASCII85Decode" -> ascii85Decode(current) ?: return null
        "/FlateDecode" -> inflateZlib(current) ?: return null
        else -> return null
      }
    }
    return current
  }

  private fun inflateZlib(data: ByteArray): ByteArray? = runCatching {
    Inflater().run {
      setInput(data)
      val output = ByteArray(maxOf(data.size * 8, 8_192))
      val length = inflate(output)
      end()
      output.copyOf(length)
    }
  }.getOrNull()

  /**
   * Decodificación ASCII85 según PDF 32000 (subset usado por ReportLab).
   */
  internal fun ascii85Decode(input: ByteArray): ByteArray? = runCatching {
    val out = ArrayList<Byte>(input.size)
    var tuple = 0L
    var count = 0
    var i = 0
    while (i < input.size) {
      val byte = input[i]
      i++
      val char = byte.toInt().toChar()
      when {
        char.isWhitespace() -> continue
        char == '<' -> continue
        char == '~' -> break
        char == 'z' -> {
          check(count == 0) { "z inside partial ASCII85 group" }
          repeat(4) { out.add(0) }
        }
        char in '!'..'u' -> {
          tuple = tuple * 85 + (char.code - '!'.code)
          count++
          if (count == 5) {
            out.add(((tuple shr 24) and 0xFF).toByte())
            out.add(((tuple shr 16) and 0xFF).toByte())
            out.add(((tuple shr 8) and 0xFF).toByte())
            out.add((tuple and 0xFF).toByte())
            tuple = 0
            count = 0
          }
        }
      }
    }
    if (count > 0) {
      var padded = tuple
      repeat(5 - count) { padded = padded * 85 + 84 }
      repeat(count - 1) { index ->
        val shift = 24 - (index * 8)
        out.add(((padded shr shift) and 0xFF).toByte())
      }
    }
    out.toByteArray()
  }.getOrNull()

  private fun extractTjStrings(bytes: ByteArray): String {
    val latin = String(bytes, Charsets.ISO_8859_1)
    return Regex("""\((?:\\.|[^\\)]){2,200}\)""")
      .findAll(latin)
      .map { decodePdfString(it.value.removePrefix("(").removeSuffix(")")) }
      .filter { it.any { ch -> ch.isLetterOrDigit() } }
      .joinToString(" ")
  }

  private fun decodePdfString(raw: String): String {
    val sb = StringBuilder()
    var i = 0
    while (i < raw.length) {
      if (raw[i] == '\\' && i + 1 < raw.length) {
        when (val next = raw[i + 1]) {
          'n' -> sb.append('\n')
          'r' -> sb.append('\r')
          't' -> sb.append('\t')
          '\\', '(', ')' -> sb.append(next)
          in '0'..'7' -> {
            val octal = buildString {
              var j = i + 1
              repeat(3) {
                if (j < raw.length && raw[j] in '0'..'7') {
                  append(raw[j])
                  j++
                }
              }
            }
            if (octal.isNotEmpty()) {
              sb.append(octal.toInt(8).toChar())
              i += octal.length
            }
          }
          else -> sb.append(next)
        }
        i += 2
      } else {
        sb.append(raw[i])
        i++
      }
    }
    return sb.toString()
  }

  private fun indexOf(bytes: ByteArray, needle: ByteArray, fromIndex: Int): Int {
    if (needle.isEmpty() || fromIndex >= bytes.size) return -1
    outer@ for (i in fromIndex..bytes.size - needle.size) {
      for (j in needle.indices) {
        if (bytes[i + j] != needle[j]) continue@outer
      }
      return i
    }
    return -1
  }

  private fun startsWith(bytes: ByteArray, offset: Int, text: String): Boolean {
    val needle = text.toByteArray(Charsets.US_ASCII)
    if (offset < 0 || offset + needle.size > bytes.size) return false
    return needle.indices.all { bytes[offset + it] == needle[it] }
  }
}
