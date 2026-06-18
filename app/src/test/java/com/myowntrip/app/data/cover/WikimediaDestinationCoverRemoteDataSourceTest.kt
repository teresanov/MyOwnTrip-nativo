package com.myowntrip.app.data.cover

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class WikimediaDestinationCoverRemoteDataSourceTest {

  private val dataSource = WikimediaDestinationCoverRemoteDataSource()

  @Test
  fun `parse thumbnail from wikimedia response`() {
    val json = """
      {
        "query": {
          "pages": {
            "123": {
              "thumbnail": {
                "source": "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ab/Example.jpg/800px-Example.jpg"
              }
            }
          }
        }
      }
    """.trimIndent()
    assertEquals(
      "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ab/Example.jpg/800px-Example.jpg",
      dataSource.parseThumbnailUrl(json),
    )
  }

  @Test
  fun `parse returns null on empty pages`() {
    assertNull(dataSource.parseThumbnailUrl("""{"query":{"pages":{}}}"""))
  }
}
