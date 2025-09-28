// Project-level build.gradle (or build.gradle.kts if you're using Kotlin DSL for project-level)

plugins {
    alias(libs.plugins.android.application) apply false
    // ADD THIS LINE for the google-services plugin:
    id("com.google.gms.google-services") version "4.4.3" apply false // IMPORTANT: Check for the latest version!
    // If you're using Kotlin at all (even just build scripts) you might have:
    // alias(libs.plugins.kotlin.android) apply false // or id("org.jetbrains.kotlin.android") version "X.X.X" apply false
}


