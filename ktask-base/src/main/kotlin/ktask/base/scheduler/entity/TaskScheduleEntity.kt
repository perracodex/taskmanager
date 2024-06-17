/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package ktask.base.scheduler.entity

import kotlinx.serialization.Serializable
import ktask.base.utils.KLocalDateTime

/**
 * Entity representing the details of a scheduled task.
 *
 * @property name The name of the task.
 * @property group The group of the task.
 * @property consumer The consumer that will execute the task.
 * @property nextFireTime The next time the task is scheduled to be executed. Or null if it is not scheduled.
 * @property state The state of the task, eg: 'PAUSED', 'NORMAL', etc.
 * @property outcome The execution outcome of the task.
 * @property log The log information of the task.
 * @property interval The interval at which the task should repeat.
 * @property intervalInfo Optional information about the interval. For example a raw cron expression.
 * @property runs The number of times the task has been triggered.
 * @property dataMap Concrete parameters of the task.
 */
@Serializable
data class TaskScheduleEntity(
    val name: String,
    val group: String,
    val consumer: String,
    val nextFireTime: KLocalDateTime?,
    val state: String,
    val outcome: String?,
    val log: String?,
    val interval: String?,
    val intervalInfo: String?,
    val runs: Int?,
    val dataMap: List<String>,
)
