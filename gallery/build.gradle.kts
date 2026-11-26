import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
  kotlin("multiplatform") version "2.3.20"
  kotlin("plugin.compose") version "2.3.20"
  id("org.jetbrains.compose") version "1.10.1"
}

val designsystemSrc = rootDir.resolve("../designsystem/src")

kotlin {
  jvm()
  @OptIn(ExperimentalWasmDsl::class)
  wasmJs {
    outputModuleName.set("gallery")
    browser {
      commonWebpackConfig {
        outputFileName = "gallery.js"
        devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
          port = 8080
        }
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
    val jvmMain by getting {
      dependsOn(skiaMain)
      dependencies {
        implementation(compose.desktop.currentOs)
      }
    }
    val wasmJsMain by getting {
      dependsOn(skiaMain)
    }
  }
}

compose.desktop {
  application {
    mainClass = "com.myowntrip.gallery.MainKt"
  }
}
