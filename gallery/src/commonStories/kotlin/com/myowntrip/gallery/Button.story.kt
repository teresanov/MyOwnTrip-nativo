package com.myowntrip.gallery

import androidx.compose.material3.Text
import com.myowntrip.app.ui.theme.MOTButton
import com.myowntrip.app.ui.theme.MOTIconButton
import com.myowntrip.app.ui.theme.MOTOutlinedButton
import com.myowntrip.app.ui.theme.MOTTextButton
import com.myowntrip.app.ui.theme.MOTTonalButton
import com.myowntrip.app.ui.theme.MyOwnTripTheme
import org.jetbrains.compose.storytale.story

val `MOTButton filled` by story(group = "Button") {
  val label by parameter("Abrir cuaderno")
  val enabled by parameter(true)
  val darkTheme by parameter(false)
  val reduceMotion by parameter(false)

  MyOwnTripTheme(darkTheme = darkTheme, reduceMotion = reduceMotion) {
    MOTButton(onClick = {}, enabled = enabled) {
      Text(label)
    }
  }
}

val `MOTTonalButton` by story(group = "Button") {
  val label by parameter("Siguiente")
  val enabled by parameter(true)
  val darkTheme by parameter(false)

  MyOwnTripTheme(darkTheme = darkTheme) {
    MOTTonalButton(onClick = {}, enabled = enabled) {
      Text(label)
    }
  }
}

val `MOTOutlinedButton` by story(group = "Button") {
  val label by parameter("Cancelar")
  val enabled by parameter(true)
  val darkTheme by parameter(false)

  MyOwnTripTheme(darkTheme = darkTheme) {
    MOTOutlinedButton(onClick = {}, enabled = enabled) {
      Text(label)
    }
  }
}

val `MOTTextButton` by story(group = "Button") {
  val label by parameter("Descartar")
  val enabled by parameter(true)
  val darkTheme by parameter(false)

  MyOwnTripTheme(darkTheme = darkTheme) {
    MOTTextButton(onClick = {}, enabled = enabled) {
      Text(label)
    }
  }
}

val `MOTIconButton` by story(group = "Button") {
  val enabled by parameter(true)
  val darkTheme by parameter(false)

  MyOwnTripTheme(darkTheme = darkTheme) {
    MOTIconButton(onClick = {}, enabled = enabled) {
      Text("✎")
    }
  }
}
