/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package taskmanager.scheduler.service

import kotlinx.serialization.Serializable
import taskmanager.base.env.HealthCheckApi

/**
 * Used to check the health of the scheduler.
 *
 * @property errors The list of errors that occurred during the check.
 * @property isStarted Whether the scheduler is started.
 * @property isPaused Whether the scheduler is paused.
 * @property totalTasks The total number of tasks in the scheduler.
 */
@HealthCheckApi
@Serializable
public class SchedulerHealth private constructor(
    public val errors: MutableList<String>,
    public val isStarted: Boolean,
    public val isPaused: Boolean,
    public val totalTasks: Int
) {
    init {
        if (!isStarted) {
            errors.add("SchedulerHealth. Scheduler is not started.")
        }
    }

    public companion object {
        /**
         * Creates a new [SchedulerHealth] instance.
         * We need to use a suspendable factory method as totalTasks is a suspend function.
         */
        public suspend fun create(): SchedulerHealth {
            return SchedulerHealth(
                errors = mutableListOf(),
                isStarted = SchedulerService.isStarted(),
                isPaused = SchedulerService.isPaused(),
                totalTasks = SchedulerService.totalTasks()
            )
        }
    }
}
