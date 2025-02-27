/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

group = "taskmanager.scheduler"
version = "1.0.0"

dependencies {
    implementation(project(":taskmanager-core:base"))
    implementation(project(":taskmanager-core:database"))

    detektPlugins(libs.detekt.formatting)

    implementation(libs.cron.descriptor)

    implementation(libs.exposed.core)
    implementation(libs.exposed.pagination)

    implementation(libs.kopapi)

    implementation(libs.kotlinx.coroutines)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization)

    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.rateLimit)
    implementation(libs.ktor.server.tests)
    implementation(libs.ktor.server.thymeleaf)

    implementation(libs.micrometer.metrics)

    implementation(libs.quartz.scheduler)

    implementation(libs.shared.commons.codec)

    testImplementation(libs.test.kotlin.junit)
    testImplementation(libs.test.mockk)
    testImplementation(libs.test.mockito.kotlin)
}
