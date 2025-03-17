import org.jetbrains.kotlin.gradle.utils.extendsFrom

plugins {
    id("java-library")
    id("eclipse")
    id("idea")
    id("maven-publish")
    id("net.neoforged.gradle.userdev") version "7.0.170"
    id("org.jetbrains.kotlin.jvm") version "2.0.21"
}

val snapshotRegex = Regex("-((beta)|(alpha))$")

val minecraftVer: String by project
val minecraftRange: String by project
val neoVer: String by project
val neoRange: String by project
val kffVer: String by project
val kffRange: String by project

val modId: String by project
val modName: String by project
val modVer: String by project
val modGroup: String by project
val modDesc: String by project
val authors: String by project
val license: String by project
val licenseUrl: String by project
val homepageUrl: String by project
val githubUrn: String by project

project.version = modVer
project.group = modGroup

repositories {
    mavenLocal()
    maven {
        name = "Kotlin for Forge"
        setUrl("https://thedarkcolour.github.io/KotlinForForge/")
    }
}

base {
    archivesName = modId
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

minecraft {
    accessTransformers.file(rootProject.file("src/main/resources/META-INF/accesstransformer.cfg"))
}


runs {
    configureEach {
        //systemProperty("forge.logging.markers", "SCAN,REGISTRIES,REGISTRYDUMP")
        systemProperty("forge.logging.console.level", "debug")

        modSource(project.sourceSets.getByName("main"))
    }

    register("client") {
        // REMINDER: devLogin and renderDoc exist if desired
        systemProperty("forge.enabledGameTestNamespaces", modId)
    }

    register("server") {
        systemProperty("forge.enabledGameTestNamespaces", modId)
        argument("--nogui")
    }

    // REMINDER: will intentionally crash if no tests are present. Can use /test to test in client or server
    register("gameTestServer") {
        systemProperty("forge.enabledGameTestNamespaces", modId)
    }

    register("data") {
        arguments.addAll(listOf("--mod", modId, "--all", "--output", file("src/generated/resources/").absolutePath, "--existing", file("src/main/resources/").absolutePath))
    }
}

sourceSets {
    main {
        resources.srcDir("src/generated/resources")
    }
}

configurations {
    runtimeClasspath.extendsFrom(named("localRuntime"))
}

// REMINDER: prefer `localRuntime` over `runtimeOnly`
dependencies {
    implementation("net.neoforged:neoforge:$neoVer")

    implementation("thedarkcolour:kotlinforforge-neoforge:$kffVer")
}

tasks {
    named<Wrapper>("wrapper").configure {
        distributionType = Wrapper.DistributionType.BIN
    }

    withType<ProcessResources>().configureEach {
        val properties = mapOf(
            "minecraftRange"    to minecraftRange,
            "neoRange"          to neoRange,
            "kffRange"          to kffRange,
            "modId"             to modId,
            "modName"           to modName,
            "modVer"            to modVer,
            "modDesc"           to modDesc,
            "authors"           to authors,
            "license"           to license,
            "homepageUrl"       to homepageUrl,
            "issuesUrl"         to "https://$githubUrn/issues"
        )

        inputs.properties(properties)

        filesMatching(listOf("META-INF/neoforge.mods.toml", "*.mixins.json")) {
            expand(properties)
        }
    }

    named("publish") {
        enabled = false
    }

    register("publishRelease") {
        if (project.version.toString().matches(snapshotRegex)) throw GradleException("Attempted to publish snapshot to release repository.")
        dependsOn("publish${rootProject.name.replaceFirstChar(Char::uppercaseChar)}PublicationToReleaseRepository}")
    }

    register("publishSnapshot") {
        if (!project.version.toString().matches(snapshotRegex)) throw GradleException("Attempted to publish release to snapshot repository.")
        dependsOn("publish${rootProject.name.replaceFirstChar(Char::uppercaseChar)}PublicationToSnapshotRepository")
    }
}

publishing {
    publications {
        register<MavenPublication>(rootProject.name) {
            groupId = modGroup
            artifactId = modId
            version = modVer

            pom {
                name = modName
                description = modDesc
                url = homepageUrl
                licenses {
                    name = license
                    url = licenseUrl
                }
                scm {
                    connection = "scm:git:git://$githubUrn.git"
                    developerConnection = "scm:git:ssh://git@$githubUrn.git"
                    url = "https://$githubUrn"
                }
            }

            from(components["java"])
        }
    }
    repositories {
        // In `gradle publishRelease`, use -PreleaseUsername={token} -PreleasePassword={secret}
        maven {
            name = "release"
            url = uri("https://maven.stultorum.com/releases")
            credentials(PasswordCredentials::class)
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
        // In `gradle publishSnapshot`, use -PsnapshotUsername={token} -PsnapshotPassword={secret}
        maven {
            name = "snapshot"
            url = uri("https://maven.stultorum.com/snapshots")
            credentials(PasswordCredentials::class)
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}
