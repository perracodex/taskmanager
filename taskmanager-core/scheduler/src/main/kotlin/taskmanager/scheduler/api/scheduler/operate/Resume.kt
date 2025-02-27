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
 * Resume all the scheduler tasks.
 */
internal fun Route.resumeSchedulerRoute() {
    post("/admin/scheduler/resume") {
        val state: TaskStateChange = SchedulerService.resume()
        call.respond(status = HttpStatusCode.OK, message = state)
    } api {
        tags = setOf("Scheduler Admin")
        summary = "Resume all scheduler tasks."
        description = "Resume all the scheduler tasks."
        operationId = "resumeScheduler"
        response<TaskStateChange>(status = HttpStatusCode.OK) {
            description = "The state change of the scheduler."
        }
    }
}
