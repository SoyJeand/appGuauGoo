// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.kotlinKapt) apply false
    // CORRECTO: Declara el plugin para todo el proyecto pero no lo aplica todavía.
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin) apply false

}