import java.util.Properties

//Load properties
private val properties = Properties()
private val localPropertyFile = project.rootProject.file("local.properties")
properties.load(localPropertyFile.inputStream())

val RELEASE_KEYSTORE_PATH = properties.getProperty("RELEASE_KEYSTORE_PATH")
  .toString()
val RELEASE_KEYSTORE_PASSWORD = properties.getProperty("RELEASE_KEYSTORE_PASSWORD")
  .toString()
val RELEASE_KEY_ALIAS = properties.getProperty("RELEASE_KEY_ALIAS")
  .toString()
val RELEASE_KEY_PASSWORD = properties.getProperty("RELEASE_KEY_PASSWORD")
  .toString()

plugins {
  id("com.android.application")
  kotlin("android")
  kotlin("kapt")
  id(KtLint.name)
}

android {
  compileSdkVersion(BuildConfig.compileSdk)

  defaultConfig {
    applicationId = "com.popstack.mvoter2015"
    minSdkVersion(BuildConfig.minSdk)
    targetSdkVersion(BuildConfig.targetSdk)
    versionCode = BuildConfig.versionCode
    versionName = BuildConfig.versionName
    resConfigs("en", "mm")
    setProperty("archivesBaseName", "mVoter-${BuildConfig.versionName}")

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    buildFeatures {
      viewBinding = true
    }

    kapt {
      arguments {
        arg("dagger.hilt.disableModulesHaveInstallInCheck", "true")
      }
    }
  }

  signingConfigs {
    register("release") {
      storeFile = File(rootDir, RELEASE_KEYSTORE_PATH)
      storePassword = RELEASE_KEYSTORE_PASSWORD
      keyAlias = RELEASE_KEY_ALIAS
      keyPassword = RELEASE_KEY_PASSWORD
    }
  }

  buildTypes {

    getByName("debug") {
      isMinifyEnabled = false
      isDebuggable = true
      versionNameSuffix = "-debug"
      applicationIdSuffix = ".debug"
    }

    getByName("release") {
      isMinifyEnabled = true
      isShrinkResources = true
      isZipAlignEnabled = true
      isDebuggable = false
      signingConfig = signingConfigs.getByName("release")
      proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
    }
  }

  compileOptions {
    isCoreLibraryDesugaringEnabled = true
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }

  kotlinOptions {
    jvmTarget = "1.8"
  }
}

dependencies {
  coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.0.10")

  implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
  implementation(project(":domain"))
  implementation(project(":data:android"))
  implementation(project(":simplespinneradapter"))

  implementation(Kotlin.stdblib_jdk)
  implementation(KotlinCoroutine.android)

  //AndroidX
  implementation(AndroidXAppCompat.app_compat)
  implementation(AndroidXCore.core_ktx)
  androidXArch()
  androidxActivity()
  androidxFragment()
  implementation(AndroidXViewPager.view_pager_2)
  implementation(AndroidXViewPager.view_pager)
  implementation(AndroidXRecyclerView.recycler_view)
  implementation(AndroidXPaging.common)
  implementation(AndroidXPaging.runtime)
  implementation(AndroidXDataStore.preferences)
  implementation("androidx.browser:browser:1.3.0-alpha06")

  implementation(Conductor.core)
  implementation(Conductor.viewpager)
  implementation(Conductor.androidx_transition)
  implementation(Conductor.lifecycle)

  //Material
  implementation(Material.material)

  //Constraint Layout
  implementation(AndroidXConstraintLayout.constraint_layout)

  //Dagger
  daggerJvm()
  daggerAndroid()

  //ThreeTenBp
  implementation(CommonLibs.timber)

  //Coil
  implementation(Coil.coil)

  implementation("com.github.chrisbanes:PhotoView:2.3.0")

  //Test
  testImplementation(CommonLibs.junit)
  testImplementation(project(":coroutinetestrule"))
  mockito()
  mockitoAndroid()
  androidXTest()
  androidXEspresso()
}

ktlint {
  android.set(true)
}