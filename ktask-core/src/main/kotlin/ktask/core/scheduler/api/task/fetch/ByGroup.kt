/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package ktask.core.scheduler.api.task.fetch

import io.github.perracodex.kopapi.dsl.operation.api
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ktask.core.scheduler.api.SchedulerRouteApi
import ktask.core.scheduler.service.SchedulerService

/**
 * Gets all scheduler task groups.
 */
@SchedulerRouteApi
internal fun Route.getSchedulerTaskGroupsRoute() {
    get("/admin/scheduler/task/group") {
        val groups: List<String> = SchedulerService.tasks.groups()
        call.respond(status = HttpStatusCode.OK, message = groups)
    } api {
        tags = setOf("Scheduler")
        summary = "Get all scheduler task groups."
        description = "Get all the scheduler task groups."
        operationId = "getSchedulerTaskGroups"
        response<List<String>>(status = HttpStatusCode.OK) {
            description = "The list of task groups."
        }
    }
}
