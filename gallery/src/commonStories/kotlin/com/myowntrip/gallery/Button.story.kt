package com.myowntrip.gallery

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.myowntrip.app.ui.theme.MOTButton
import com.myowntrip.app.ui.theme.MOTButtonSize
import com.myowntrip.app.ui.theme.MOTIconButton
import com.myowntrip.app.ui.theme.MOTIconButtonSize
import com.myowntrip.app.ui.theme.MOTIconButtonType
import com.myowntrip.app.ui.theme.MOTIconButtonWidth
import com.myowntrip.app.ui.theme.MOTIcons
import com.myowntrip.app.ui.theme.MOTOutlinedButton
import com.myowntrip.app.ui.theme.MOTTextButton
import com.myowntrip.app.ui.theme.MOTTonalButton
import com.myowntrip.app.ui.theme.MyOwnTripTheme
import org.jetbrains.compose.storytale.story

@Composable
private fun MotButtonSlot(
  label: String,
  size: MOTButtonSize,
  showIcon: Boolean,
  loading: Boolean,
  errorLabel: Boolean = false,
) {
  if (loading) {
    CircularProgressIndicator(
      modifier = Modifier.size(18.dp),
      strokeWidth = 2.dp,
      color = MaterialTheme.colorScheme.onPrimary,
    )
    Spacer(Modifier.width(size.leadingIconGap))
  } else if (showIcon) {
    Icon(
      MOTIcons.Add,
      contentDescription = null,
      modifier = Modifier.size(size.leadingIconSize),
    )
    Spacer(Modifier.width(size.leadingIconGap))
  }
  Text(
    text = label,
    color = if (errorLabel) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onPrimary,
  )
}

@Composable
private fun MotTonalSlot(label: String, size: MOTButtonSize, showIcon: Boolean) {
  if (showIcon) {
    Icon(MOTIcons.Add, contentDescription = null, modifier = Modifier.size(size.leadingIconSize))
    Spacer(Modifier.width(size.leadingIconGap))
  }
  Text(label)
}

private fun buttonSize(name: String) =
  MOTButtonSize.entries.find { it.name.equals(name, ignoreCase = true) } ?: MOTButtonSize.Small

private fun iconSize(name: String) =
  MOTIconButtonSize.entries.find { it.name.equals(name, ignoreCase = true) } ?: MOTIconButtonSize.Small

private fun iconWidth(name: String) =
  MOTIconButtonWidth.entries.find { it.name.equals(name, ignoreCase = true) } ?: MOTIconButtonWidth.Default

private fun iconType(name: String) =
  MOTIconButtonType.entries.find { it.name.equals(name, ignoreCase = true) } ?: MOTIconButtonType.Square

val `MOTButton` by story(group = "Button") {
  val label by parameter("Button")
  val size by parameter("Small")
  val showIcon by parameter(true)
  val enabled by parameter(true)
  val loading by parameter(false)
  val fillMaxWidth by parameter(false)
  val darkTheme by parameter(false)
  val reduceMotion by parameter(false)
  val motSize = buttonSize(size)

  MyOwnTripTheme(darkTheme = darkTheme, reduceMotion = reduceMotion) {
    MOTButton(
      onClick = {},
      enabled = enabled && !loading,
      size = motSize,
      modifier = if (fillMaxWidth) Modifier.fillMaxWidth() else Modifier,
    ) {
      MotButtonSlot(label, motSize, showIcon, loading)
    }
  }
}

val `MOTButton · XSmall` by story(group = "Button") {
  val darkTheme by parameter(false)
  MyOwnTripTheme(darkTheme = darkTheme) {
    MOTButton(onClick = {}, size = MOTButtonSize.XSmall) {
      MotButtonSlot("Button", MOTButtonSize.XSmall, showIcon = true, loading = false)
    }
  }
}

val `MOTButton · Small` by story(group = "Button") {
  val darkTheme by parameter(false)
  MyOwnTripTheme(darkTheme = darkTheme) {
    MOTButton(onClick = {}, size = MOTButtonSize.Small) {
      MotButtonSlot("Button", MOTButtonSize.Small, showIcon = true, loading = false)
    }
  }
}

val `MOTButton · Medium` by story(group = "Button") {
  val darkTheme by parameter(false)
  MyOwnTripTheme(darkTheme = darkTheme) {
    MOTButton(onClick = {}, size = MOTButtonSize.Medium) {
      MotButtonSlot("Button", MOTButtonSize.Medium, showIcon = true, loading = false)
    }
  }
}

val `MOTButton · Large` by story(group = "Button") {
  val darkTheme by parameter(false)
  MyOwnTripTheme(darkTheme = darkTheme) {
    MOTButton(onClick = {}, size = MOTButtonSize.Large) {
      MotButtonSlot("Button", MOTButtonSize.Large, showIcon = true, loading = false)
    }
  }
}

val `MOTButton · Disabled` by story(group = "Button") {
  val darkTheme by parameter(false)
  MyOwnTripTheme(darkTheme = darkTheme) {
    MOTButton(onClick = {}, enabled = false) {
      MotButtonSlot("Button", MOTButtonSize.Small, showIcon = true, loading = false)
    }
  }
}

val `MOTButton · Loading` by story(group = "Button") {
  val darkTheme by parameter(false)
  MyOwnTripTheme(darkTheme = darkTheme) {
    MOTButton(onClick = {}, enabled = false) {
      MotButtonSlot("Button", MOTButtonSize.Small, showIcon = false, loading = true)
    }
  }
}

val `MOTTonalButton` by story(group = "Button") {
  val label by parameter("Button")
  val size by parameter("XSmall")
  val showIcon by parameter(true)
  val enabled by parameter(true)
  val darkTheme by parameter(false)
  val motSize = buttonSize(size)

  MyOwnTripTheme(darkTheme = darkTheme) {
    MOTTonalButton(onClick = {}, enabled = enabled, size = motSize) {
      MotTonalSlot(label, motSize, showIcon)
    }
  }
}

val `MOTOutlinedButton` by story(group = "Button") {
  val label by parameter("Button")
  val size by parameter("Small")
  val showIcon by parameter(true)
  val enabled by parameter(true)
  val darkTheme by parameter(false)
  val motSize = buttonSize(size)

  MyOwnTripTheme(darkTheme = darkTheme) {
    MOTOutlinedButton(onClick = {}, enabled = enabled, size = motSize) {
      MotTonalSlot(label, motSize, showIcon)
    }
  }
}

val `MOTTextButton` by story(group = "Button") {
  val label by parameter("Button")
  val size by parameter("Small")
  val showIcon by parameter(true)
  val enabled by parameter(true)
  val errorLabel by parameter(false)
  val darkTheme by parameter(false)
  val motSize = buttonSize(size)

  MyOwnTripTheme(darkTheme = darkTheme) {
    MOTTextButton(onClick = {}, enabled = enabled, size = motSize) {
      if (showIcon) {
        Icon(MOTIcons.Add, contentDescription = null, modifier = Modifier.size(motSize.leadingIconSize))
        Spacer(Modifier.width(motSize.leadingIconGap))
      }
      Text(
        text = label,
        color = if (errorLabel) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
      )
    }
  }
}

val `MOTTextButton · Error` by story(group = "Button") {
  val darkTheme by parameter(false)
  MyOwnTripTheme(darkTheme = darkTheme) {
    MOTTextButton(onClick = {}) {
      Text("Button", color = MaterialTheme.colorScheme.error)
    }
  }
}

val `MOTIconButton` by story(group = "Button") {
  val size by parameter("Small")
  val width by parameter("Default")
  val type by parameter("Square")
  val enabled by parameter(true)
  val darkTheme by parameter(false)
  val reduceMotion by parameter(false)

  MyOwnTripTheme(darkTheme = darkTheme, reduceMotion = reduceMotion) {
    MOTIconButton(
      onClick = {},
      enabled = enabled,
      size = iconSize(size),
      width = iconWidth(width),
      type = iconType(type),
    ) {
      Icon(MOTIcons.Edit, contentDescription = "Edit")
    }
  }
}

val `MOTIconButton · Square Small Default` by story(group = "Button") {
  val darkTheme by parameter(false)
  MyOwnTripTheme(darkTheme = darkTheme) {
    MOTIconButton(onClick = {}) {
      Icon(MOTIcons.Edit, contentDescription = "Edit")
    }
  }
}

val `MOTIconButton · Square Small Narrow` by story(group = "Button") {
  val darkTheme by parameter(false)
  MyOwnTripTheme(darkTheme = darkTheme) {
    MOTIconButton(onClick = {}, width = MOTIconButtonWidth.Narrow) {
      Icon(MOTIcons.Edit, contentDescription = "Edit")
    }
  }
}

val `MOTIconButton · Square Small Wide` by story(group = "Button") {
  val darkTheme by parameter(false)
  MyOwnTripTheme(darkTheme = darkTheme) {
    MOTIconButton(onClick = {}, width = MOTIconButtonWidth.Wide) {
      Icon(MOTIcons.Edit, contentDescription = "Edit")
    }
  }
}

val `MOTIconButton · Round Small Default` by story(group = "Button") {
  val darkTheme by parameter(false)
  MyOwnTripTheme(darkTheme = darkTheme) {
    MOTIconButton(onClick = {}, type = MOTIconButtonType.Round) {
      Icon(MOTIcons.Edit, contentDescription = "Edit")
    }
  }
}

val `MOTIconButton · Square Medium Default` by story(group = "Button") {
  val darkTheme by parameter(false)
  MyOwnTripTheme(darkTheme = darkTheme) {
    MOTIconButton(onClick = {}, size = MOTIconButtonSize.Medium) {
      Icon(MOTIcons.Edit, contentDescription = "Edit")
    }
  }
}

val `MOTIconButton · Square Large Default` by story(group = "Button") {
  val darkTheme by parameter(false)
  MyOwnTripTheme(darkTheme = darkTheme) {
    MOTIconButton(onClick = {}, size = MOTIconButtonSize.Large) {
      Icon(MOTIcons.Edit, contentDescription = "Edit")
    }
  }
}

val `MOTIconButton · Square XSmall Default` by story(group = "Button") {
  val darkTheme by parameter(false)
  MyOwnTripTheme(darkTheme = darkTheme) {
    MOTIconButton(onClick = {}, size = MOTIconButtonSize.XSmall) {
      Icon(MOTIcons.Edit, contentDescription = "Edit")
    }
  }
}

val `MOTIconButton · Disabled` by story(group = "Button") {
  val darkTheme by parameter(false)
  MyOwnTripTheme(darkTheme = darkTheme) {
    MOTIconButton(onClick = {}, enabled = false) {
      Icon(MOTIcons.Edit, contentDescription = "Edit")
    }
  }
}
