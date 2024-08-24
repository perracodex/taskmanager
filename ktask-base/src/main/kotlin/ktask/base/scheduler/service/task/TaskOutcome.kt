/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package ktask.base.scheduler.service.task

/**
 * Represents a scheduler task outcome status.
 */
public enum class TaskOutcome {
    /** The task has completed successfully. */
    SUCCESS,

    /** The task has failed to complete. */
    ERROR,
}
