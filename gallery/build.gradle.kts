import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
  kotlin("multiplatform") version "2.3.20"
  kotlin("plugin.compose") version "2.3.20"
  id("org.jetbrains.compose") version "1.10.1"
  id("io.github.aryapreetam.storytale") version "0.0.7"
}

kotlin {
  jvm()
  @OptIn(ExperimentalWasmDsl::class)
  wasmJs {
    browser()
    binaries.executable()
  }

  sourceSets {
    commonMain.dependencies {
      implementation(compose.runtime)
      implementation(compose.foundation)
      implementation(compose.material3)
      implementation(compose.ui)
      implementation(compose.components.resources)
    }
  }
}
