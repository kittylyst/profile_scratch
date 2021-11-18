plugins {
  `java`
  `application`
  id("com.github.johnrengelman.shadow") version "5.2.0"
}

repositories {
  mavenCentral()
}

java {
    modularity.inferModulePath.set(true)
}

sourceSets {
  main {
    java {
      setSrcDirs(listOf("src/main/java/"))
    }
  }
}

project.setProperty("mainClassName", "com.your.MainClass")

dependencies {
  implementation("com.google.protobuf:protobuf-java:3.18.1")

  testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.1")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.1")
}

tasks {
  withType<JavaCompile> {
    options.compilerArgs = listOf()
    options.release.set(17)
  }

  withType<Javadoc>().configureEach {
    with(options as StandardJavadocDocletOptions) {
      source = "17"
    }
  }

  shadowJar {
//    mainClassName("io.opentelemetry.experimental.JfrProfilerAgent")
    manifest {
      attributes(jar.get().manifest.attributes)
      attributes(
        "Agent-Class" to "io.opentelemetry.experimental.JfrProfilerAgent",
        "Premain-Class" to "io.opentelemetry.experimental.JfrProfilerAgent",
        "Can-Redefine-Classes" to true,
        "Can-Retransform-Classes" to true
      )
    }
    exclude("**/module-info.class")
    exclude("module-info.class")
  }

  test {
    useJUnitPlatform()
  }
}
