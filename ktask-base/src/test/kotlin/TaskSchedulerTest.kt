/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

import io.ktor.test.dispatcher.*
import kotlinx.coroutines.delay
import ktask.base.persistence.serializers.SUUID
import ktask.base.scheduler.service.core.SchedulerService
import ktask.base.scheduler.service.request.SchedulerRequest
import ktask.base.scheduler.service.schedule.Schedule
import ktask.base.scheduler.service.schedule.TaskStartAt
import ktask.base.scheduler.service.task.SchedulerTask
import ktask.base.utils.TestUtils
import org.quartz.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class SchedulerServiceTest {

    companion object {
        private val testResults = mutableListOf<String>()
    }

    @BeforeTest
    fun setUp() {
        TestUtils.loadSettings()
        TestUtils.setupDatabase()
        SchedulerService.start()
    }

    @AfterTest
    fun tearDown() {
        TestUtils.tearDown()
        SchedulerService.stop()
    }

    @Test
    fun testImmediate(): Unit = testSuspend {
        val uniqueTestKey = "uniqueTestTask_${System.nanoTime()}"

        val taskId: SUUID = SUUID.randomUUID()
        val jobKey: JobKey = SchedulerRequest(
            taskId = taskId,
            taskClass = SimpleTestTask::class.java,
            startAt = TaskStartAt.Immediate,
            parameters = mapOf("uniqueKey" to uniqueTestKey)
        ).send()

        // Wait for enough time to allow the task to execute.
        delay(timeMillis = 3000L)

        assertTrue(actual = testResults.contains(uniqueTestKey))

        // Clean up by un-scheduling the task.
        SchedulerService.deleteTask(name = jobKey.name, group = jobKey.group)
    }

    @Test
    fun testInterval(): Unit = testSuspend {
        val uniqueTestKey = "uniqueTestTask_${System.nanoTime()}"

        val interval: Schedule.Interval = Schedule.Interval(days = 0u, hours = 0u, minutes = 0u, seconds = 1u)
        val taskId: SUUID = SUUID.randomUUID()
        val jobKey: JobKey = SchedulerRequest(
            taskId = taskId,
            taskClass = SimpleTestTask::class.java,
            startAt = TaskStartAt.Immediate,
            parameters = mapOf("uniqueKey" to uniqueTestKey)
        ).send(schedule = interval)

        // Wait for enough time to allow the task to execute.
        delay(timeMillis = 3000L)

        assertTrue(actual = testResults.contains(uniqueTestKey))

        // Clean up by un-scheduling the task.
        SchedulerService.deleteTask(name = jobKey.name, group = jobKey.group)
    }

    @Test
    fun testCron(): Unit = testSuspend {
        val uniqueTestKey = "uniqueTestTask_${System.nanoTime()}"

        val cron: Schedule.Cron = Schedule.Cron(cron = "0/1 * * * * ?")
        val taskId: SUUID = SUUID.randomUUID()
        val jobKey: JobKey = SchedulerRequest(
            taskId = taskId,
            taskClass = SimpleTestTask::class.java,
            startAt = TaskStartAt.Immediate,
            parameters = mapOf("uniqueKey" to uniqueTestKey)
        ).send(schedule = cron)

        // Wait for enough time to allow the task to execute.
        delay(timeMillis = 3000L)

        assertTrue(actual = testResults.contains(uniqueTestKey))

        // Clean up by un-scheduling the task.
        SchedulerService.deleteTask(name = jobKey.name, group = jobKey.group)
    }

    @Test
    fun testTaskMisfireHandling(): Unit = testSuspend {
        val jobKey: JobKey = JobKey.jobKey("misfireTestJob", "testGroup")
        val jobDetail: JobDetail = JobBuilder.newJob(MisfireTestTask::class.java)
            .withIdentity(jobKey)
            .build()

        // Configure the trigger to misfire by using a tight schedule and a short misfire threshold.
        val trigger = TriggerBuilder.newTrigger()
            .withIdentity("misfireTestTrigger", "testGroup")
            .startAt(DateBuilder.futureDate(2, DateBuilder.IntervalUnit.SECOND)) // Start in the near future.
            .withSchedule(
                SimpleScheduleBuilder.simpleSchedule()
                    .withMisfireHandlingInstructionFireNow()
            ) // Set misfire instruction.
            .build()

        SchedulerService.newTask(task = jobDetail, trigger = trigger)

        // Wait enough time to ensure the task has time to misfire plus some buffer.
        delay(timeMillis = 4000L)

        // Verify the task misfire has been handled.
        assertTrue(actual = testResults.contains(MisfireTestTask.MISFIRE_MESSAGE))

        // Clean up
        SchedulerService.deleteTask(name = jobKey.name, group = jobKey.group)
    }

    class MisfireTestTask : Job {
        companion object {
            const val MISFIRE_MESSAGE: String = "Misfire_Handled"
            private const val REGULAR_EXECUTION_MESSAGE: String = "Regular_Execution"
        }

        override fun execute(context: JobExecutionContext?) {
            // Check if this execution is a misfire.
            if (context?.trigger?.nextFireTime == null) {
                // This means the task has been fired due to misfire handling.
                println("TEST PASSED: Misfire handled for task: ${context!!.jobDetail.key}")
                testResults.add(MISFIRE_MESSAGE)
            } else {
                // Regular execution.
                println("TEST FAILED: Expected misfire handling but got regular execution for task: ${context.jobDetail.key}")
                testResults.add(REGULAR_EXECUTION_MESSAGE)
            }
        }
    }

    class SimpleTestTask : SchedulerTask() {
        override fun start(properties: Map<String, Any>) {
            val uniqueKey: String = (properties["uniqueKey"] ?: "defaultKey") as String
            testResults.add(uniqueKey)
            println("Test task executed with unique key: $uniqueKey")
        }
    }
}
