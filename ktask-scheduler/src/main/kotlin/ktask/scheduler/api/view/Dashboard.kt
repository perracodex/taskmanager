/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package ktask.scheduler.api.view

import io.github.perracodex.kopapi.dsl.operation.api
import io.github.perracodex.kopapi.dsl.parameter.queryParameter
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.thymeleaf.*
import ktask.core.persistence.util.toUuidOrNull
import ktask.scheduler.model.task.TaskSchedule
import ktask.scheduler.service.SchedulerService
import kotlin.uuid.Uuid

/**
 * The scheduler dashboard route.
 */
internal fun Route.schedulerDashboardRoute() {
    get("/admin/scheduler/dashboard") {
        val groupId: Uuid? = call.queryParameters["groupId"].toUuidOrNull()
        val tasks: List<TaskSchedule> = SchedulerService.tasks.all(groupId = groupId)
        val content = ThymeleafContent(template = "scheduler/dashboard", model = mapOf("data" to tasks))
        call.respond(message = content)
    } api {
        tags = setOf("Scheduler Admin")
        summary = "Get the scheduler dashboard."
        description = "Get the scheduler dashboard, listing all scheduled tasks."
        operationId = "getSchedulerDashboard"
        queryParameter<Uuid>(name = "groupId") {
            description = "The group ID to filter tasks by."
            required = false
        }
        response<String>(status = HttpStatusCode.OK) {
            description = "The scheduler dashboard."
        }
    }
}
