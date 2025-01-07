/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package ktask.scheduler.api.task.delete

import io.github.perracodex.kopapi.dsl.operation.api
import io.github.perracodex.kopapi.dsl.parameter.queryParameter
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import ktask.base.util.toUuid
import ktask.scheduler.service.SchedulerService
import kotlin.uuid.Uuid

/**
 * Deletes all scheduler tasks in a group.
 */
internal fun Route.deleteSchedulerGroupRoute() {
    delete("/admin/scheduler/group") {
        val groupId: Uuid = call.queryParameters.getOrFail(name = "groupId").toUuid()
        val deletedCount: Int = SchedulerService.tasks.delete(groupId = groupId)
        call.respond(status = HttpStatusCode.OK, message = deletedCount)
    } api {
        tags = setOf("Scheduler")
        summary = "Delete a scheduler group."
        description = "Deletes all scheduler tasks in a group."
        operationId = "deleteSchedulerGroup"
        queryParameter<Uuid>(name = "groupId") {
            description = "The group of the task."
        }
        response<Int>(status = HttpStatusCode.OK) {
            description = "The number of tasks deleted."
        }
    }
}
