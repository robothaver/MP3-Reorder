plugins {
    java
    application
    alias(libs.plugins.javamodularity)
    alias(libs.plugins.javafx)
    alias(libs.plugins.jlink)
    alias(libs.plugins.lombok)
}

group = "com.robothaver"
version = "1.0"

repositories {
    mavenCentral()
}

val junitVersion = "5.10.2"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

application {
    mainModule.set("com.robothaver.mp3reorder")
    mainClass.set("com.robothaver.mp3reorder.MP3Reorder")
}

javafx {
    version = "25"
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.media")
}

dependencies {
    implementation(libs.mp3agic)
    implementation(libs.log4j.api)
    implementation(libs.log4j.core)
    implementation(libs.atlantafx.base)
    implementation(libs.ikonli.javafx)
    implementation(libs.ikonli.feather.pack)

    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.processResources {
    filesMatching("application.properties") {
        expand(project.properties)
    }
}

tasks.run {
    jvmArgs = listOf("--enable-native-access=javafx.graphics", "--enable-native-access=javafx.media")
}

jlink {
    imageZip.set(layout.buildDirectory.file("/distributions/app-${javafx.platform.classifier}.zip"))
    options.set(listOf("--strip-debug", "--compress", "zip-9", "--no-header-files", "--no-man-pages"))
    launcher {
        name = "MP3 Reorder"
        jvmArgs = listOf("--enable-native-access=javafx.graphics", "--enable-native-access=javafx.media")
    }
    jpackage {
        val osName = System.getProperty("os.name").lowercase()
        val iconFile = when {
            osName.contains("win") -> "src/main/resources/images/logo.ico"
            osName.contains("mac") -> "src/main/resources/images/logo.icns"
            else -> "src/main/resources/images/logo.png"
        }
        imageName = "MP3Reorder"
        imageOptions = listOf("--icon", iconFile)
    }
}
