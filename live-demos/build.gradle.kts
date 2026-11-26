import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
  kotlin("multiplatform") version "2.3.20"
  kotlin("plugin.compose") version "2.3.20"
  id("org.jetbrains.compose") version "1.10.1"
}

// Kit compartido: el mismo código de :designsystem que usa la app (sin copiar nada aquí).
val designsystemSrc = rootDir.resolve("../designsystem/src")

kotlin {
  @OptIn(ExperimentalWasmDsl::class)
  wasmJs {
    outputModuleName.set("demos")
    browser {
      commonWebpackConfig {
        outputFileName = "demos.js"
      }
    }
    binaries.executable()
  }

  sourceSets {
    val commonMain by getting {
      kotlin.srcDir(designsystemSrc.resolve("commonMain/kotlin"))
      dependencies {
        implementation(compose.runtime)
        implementation(compose.foundation)
        implementation(compose.material3)
        implementation(compose.ui)
        implementation(compose.components.resources)
      }
    }
    val skiaMain by creating {
      dependsOn(commonMain)
      kotlin.srcDir(designsystemSrc.resolve("skiaMain/kotlin"))
    }
    val wasmJsMain by getting {
      dependsOn(skiaMain)
      // Los .ttf reales de la app se sirven junto al bundle (font/inter.ttf…).
      resources.srcDir(designsystemSrc.resolve("androidMain/res"))
    }
  }
}
