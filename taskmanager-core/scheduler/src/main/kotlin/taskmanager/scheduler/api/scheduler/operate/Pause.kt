/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package taskmanager.scheduler.api.scheduler.operate

import io.github.perracodex.kopapi.dsl.operation.api
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import taskmanager.scheduler.model.task.TaskStateChange
import taskmanager.scheduler.service.SchedulerService

/**
 * Pauses all the scheduler tasks.
 */
internal fun Route.pauseSchedulerRoute() {
    post("/admin/scheduler/pause") {
        val state: TaskStateChange = SchedulerService.pause()
        call.respond(status = HttpStatusCode.OK, message = state)
    } api {
        tags = setOf("Scheduler Admin")
        summary = "Pause all scheduler tasks."
        description = "Pause all the scheduler tasks."
        operationId = "pauseScheduler"
        response<TaskStateChange>(status = HttpStatusCode.OK) {
            description = "The state change of the scheduler."
        }
    }
}
