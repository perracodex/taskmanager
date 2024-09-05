/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package ktask.base.scheduler.routing

import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import ktask.base.scheduler.routing.scheduler.*
import ktask.base.scheduler.routing.tasks.delete.deleteAllSchedulerTasksRoute
import ktask.base.scheduler.routing.tasks.delete.deleteSchedulerTaskRoute
import ktask.base.scheduler.routing.tasks.get.getSchedulerTaskGroupsRoute
import ktask.base.scheduler.routing.tasks.get.getSchedulerTasksRoute
import ktask.base.scheduler.routing.tasks.operate.pauseSchedulerTaskRoute
import ktask.base.scheduler.routing.tasks.operate.resendSchedulerTaskRoute
import ktask.base.scheduler.routing.tasks.operate.resumeSchedulerTaskRoute
import ktask.base.scheduler.routing.view.schedulerDashboardRoute

/**
 * Route administers all scheduled tasks, allowing to list and delete them.
 */
public fun Route.schedulerRoutes() {

    // Sets up the routing to serve resources as static content for the scheduler.
    staticResources(remotePath = "/templates/scheduler", basePackage = "/templates/scheduler")

    // Administration related routes.
    schedulerDashboardRoute()
    schedulerStateRoute()
    pauseSchedulerRoute()
    resumeSchedulerRoute()
    restartSchedulerRoute()
    schedulerAuditRoute()

    // Task related routes.
    getSchedulerTasksRoute()
    getSchedulerTaskGroupsRoute()
    deleteSchedulerTaskRoute()
    deleteAllSchedulerTasksRoute()
    pauseSchedulerTaskRoute()
    resumeSchedulerTaskRoute()
    resendSchedulerTaskRoute()
}
