/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package ktask.base.scheduler.audit

import ktask.base.database.schema.SchedulerAuditTable
import ktask.base.scheduler.annotation.SchedulerAPI
import ktask.base.scheduler.audit.model.AuditDto
import ktask.base.scheduler.audit.model.AuditRequest
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.uuid.Uuid

/**
 * Repository to manage the persistence aND retrieval of the scheduler audit logs.
 */
@SchedulerAPI
internal object AuditRepository {

    /**
     * Creates a new audit entry.
     *
     * @param request The [AuditRequest] to create.
     */
    fun create(request: AuditRequest): Uuid {
        return transaction {
            val logId: Uuid = SchedulerAuditTable.insert {
                it[taskName] = request.taskName
                it[taskGroup] = request.taskGroup
                it[fireTime] = request.fireTime
                it[runTime] = request.runTime
                it[outcome] = request.outcome
                it[log] = request.log
                it[detail] = request.detail
            } get SchedulerAuditTable.id

            logId
        }
    }

    /**
     * Finds all the audit entries, ordered bby the most recent first.
     *
     * @return The list of [AuditDto] instances.
     */
    fun findAll(): List<AuditDto> {
        return transaction {
            SchedulerAuditTable.selectAll()
                .orderBy(SchedulerAuditTable.createdAt to SortOrder.DESC)
                .map {
                    AuditDto.from(row = it)
                }
        }
    }

    /**
     * Finds all the audit logs for a concrete task by name and group, ordered by the most recent first.
     *
     * @param taskName The name of the task.
     * @param taskGroup The group of the task.
     * @return The list of [AuditDto] instances, or an empty list if none found.
     */
    fun find(taskName: String, taskGroup: String): List<AuditDto> {
        return transaction {
            SchedulerAuditTable.selectAll()
                .where { SchedulerAuditTable.taskName eq taskName }
                .andWhere { SchedulerAuditTable.taskGroup eq taskGroup }
                .orderBy(SchedulerAuditTable.createdAt to SortOrder.DESC)
                .map {
                    AuditDto.from(row = it)
                }
        }
    }

    /**
     * Finds the most recent audit log for a specific task.
     *
     * @param taskName The name of the task.
     * @param taskGroup The group of the task.
     * @return The most recent [AuditDto] instance, or `null` if none found.
     */
    fun mostRecent(taskName: String, taskGroup: String): AuditDto? {
        return transaction {
            SchedulerAuditTable.selectAll()
                .where { SchedulerAuditTable.taskName eq taskName }
                .andWhere { SchedulerAuditTable.taskGroup eq taskGroup }
                .orderBy(SchedulerAuditTable.createdAt to SortOrder.DESC)
                .limit(n = 1)
                .map {
                    AuditDto.from(row = it)
                }.singleOrNull()
        }
    }

    /**
     * Returns the total count of audit entries for a specific task.
     *
     * @param taskName The name of the task.
     * @param taskGroup The group of the task.
     * @return The total count of audit entries for the task.
     */
    fun count(taskName: String, taskGroup: String): Int {
        return transaction {
//            addLogger(StdOutSqlLogger)
//
//            explain {
//                SchedulerAuditTable
//                    .selectAll()
//                    .where { SchedulerAuditTable.taskName eq taskName }
//                    .andWhere { SchedulerAuditTable.taskGroup eq taskGroup }
//            }.forEach(::print)


            SchedulerAuditTable
                .selectAll()
                .where { SchedulerAuditTable.taskName eq taskName }
                .andWhere { SchedulerAuditTable.taskGroup eq taskGroup }
                .count()
                .toInt()
        }
    }
}
