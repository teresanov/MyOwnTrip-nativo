package com.myowntrip.gallery

import com.myowntrip.gallery.embed.liveHosts
import com.myowntrip.gallery.embed.mountLiveSpecimens

/**
 * Demos vivas del showcase: la ficha declara huecos `data-mot-specimen` y aquí se monta,
 * en cada uno, el componente real de :designsystem.
 */
fun main() {
  val hosts = liveHosts()
  if (hosts.isNotEmpty()) mountLiveSpecimens(hosts)
}
