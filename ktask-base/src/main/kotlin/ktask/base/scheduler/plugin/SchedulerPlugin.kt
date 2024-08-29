/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package ktask.base.scheduler.plugin

import io.ktor.server.application.*
import ktask.base.scheduler.service.core.SchedulerService

/**
 * Custom Ktor plugin to configure the task scheduler.
 */
internal val SchedulerPlugin: ApplicationPlugin<SchedulerPluginConfig> = createApplicationPlugin(
    name = "SchedulerPlugin",
    ::SchedulerPluginConfig
) {
    SchedulerService.configure(environment = application.environment)

    if (pluginConfig.autoStart) {
        SchedulerService.start()
    }
}

/**
 * Configuration for the task scheduler plugin.
 *
 * @property autoStart Whether the scheduler should start automatically after configuration.
 */
internal class SchedulerPluginConfig {
    var autoStart: Boolean = false
}
