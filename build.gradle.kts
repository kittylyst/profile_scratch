plugins {
  `java`
  `application`
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

dependencies {
  // implementation("org.ow2.asm:asm:9.2")
  // implementation("org.ow2.asm:asm-commons:9.2")
  implementation("org.slf4j:slf4j-api:1.7.32")

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

  jar {
    manifest {
      attributes(jar.get().manifest.attributes)
      attributes(
        "Main-Class" to "io.opentelemetry.experimental.JfrProfilerAgent",
        "Agent-Class" to "io.opentelemetry.experimental.JfrProfilerAgent",
        "Premain-Class" to "io.opentelemetry.experimental.JfrProfilerAgent",
        "Can-Redefine-Classes" to true,
        "Can-Retransform-Classes" to true
      )
    }

  }

  test {
    useJUnitPlatform()
  }
}
