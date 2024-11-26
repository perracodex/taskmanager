/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

group = "ktask.database"
version = "1.0.0"

dependencies {
    implementation(project(":ktask-core"))

    detektPlugins(libs.detekt.formatting)

    implementation(libs.database.h2)

    implementation(libs.ktor.config)

    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.json)
    implementation(libs.exposed.kotlin.datetime)
    implementation(libs.exposed.pagination)
    implementation(libs.flyway.core)

    implementation(libs.hikariCP)

    implementation(libs.micrometer.metrics)
    implementation(libs.micrometer.metrics.prometheus)

    implementation(libs.shared.commons.codec)

    implementation(libs.typesafe.config)

    testImplementation(libs.test.kotlin.junit)
    testImplementation(libs.test.mockk)
    testImplementation(libs.test.mockito.kotlin)
}
