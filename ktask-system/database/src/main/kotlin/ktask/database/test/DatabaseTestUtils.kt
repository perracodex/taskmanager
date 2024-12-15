/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package ktask.database.test

import ktask.base.settings.AppSettings
import ktask.database.schema.scheduler.SchedulerAuditTable
import ktask.database.service.DatabaseService

/**
 * Common utilities for unit testing.
 */
public object DatabaseTestUtils {

    /**
     * Sets up the database for testing.
     */
    public fun setupDatabase() {
        DatabaseService.init(
            settings = AppSettings.database,
            environment = AppSettings.runtime.environment
        ) {
            addTable(table = SchedulerAuditTable)
        }
    }

    /**
     * Close the database connection.
     */
    public fun closeDatabase() {
        DatabaseService.close()
    }
}
