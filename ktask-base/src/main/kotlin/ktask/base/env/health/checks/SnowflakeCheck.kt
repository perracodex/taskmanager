/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package ktask.base.env.health.checks

import kotlinx.serialization.Serializable
import ktask.base.env.health.annotation.HealthCheckAPI
import ktask.base.snowflake.SnowflakeData
import ktask.base.snowflake.SnowflakeFactory

/**
 * A health check that generates a snowflake id and parses it.
 *
 * @property errors A list of errors that occurred during the check.
 * @property testId A generated snowflake id at the time of the check.
 * @property testResult The parsed snowflake data from the testId.
 * @property timestampEpoch The timestamp epoch used to generate the snowflake id.
 * @property nanoTimeStart The nano time start used to generate the snowflake id.
 */
@HealthCheckAPI
@Serializable
public data class SnowflakeCheck(
    val errors: MutableList<String> = mutableListOf(),
    var testId: String? = null,
    var testResult: SnowflakeData? = null,
    val timestampEpoch: Long = SnowflakeFactory.timestampEpoch,
    val nanoTimeStart: Long = SnowflakeFactory.nanoTimeStart,
) {
    init {
        // Attempt to generate testId and handle any exceptions.
        try {
            val generatedId: String = SnowflakeFactory.nextId()
            testId = generatedId
            testResult = SnowflakeFactory.parse(id = generatedId)
        } catch (ex: Exception) {
            errors.add(
                "${SnowflakeCheck::class.simpleName}: ${ex.message} - " +
                        "timestampEpoch: $timestampEpoch, nanoTimeStart: $nanoTimeStart."
            )
            // If any step fails, assign null to both to ensure consistency.
            testId = null
            testResult = null
        }
    }
}
