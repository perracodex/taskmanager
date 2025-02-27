/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package taskmanager.scheduler.api.scheduler.audit

import io.github.perracodex.kopapi.dsl.operation.api
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.perracodex.exposed.pagination.Page
import io.perracodex.exposed.pagination.getPageable
import taskmanager.scheduler.audit.AuditService
import taskmanager.scheduler.model.audit.AuditLog

/**
 * Returns all existing audit logs for the scheduler.
 */
internal fun Route.schedulerAllAuditRoute() {
    get("/admin/scheduler/audit") {
        val audit: Page<AuditLog> = AuditService.findAll(pageable = call.getPageable())
        call.respond(status = HttpStatusCode.OK, message = audit)
    } api {
        tags = setOf("Scheduler Admin")
        summary = "Get all scheduler audit logs."
        description = "Get all existing audit logs for the scheduler."
        operationId = "getAllSchedulerAuditLogs"
        response<Page<AuditLog>>(status = HttpStatusCode.OK) {
            description = "Existing scheduler audit logs."
        }
    }
}
