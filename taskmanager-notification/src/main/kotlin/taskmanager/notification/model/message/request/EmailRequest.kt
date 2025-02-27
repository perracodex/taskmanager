/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package taskmanager.notification.model.message.request

import kotlinx.serialization.Serializable
import taskmanager.base.error.validator.EmailValidator
import taskmanager.base.serializer.NoBlankString
import taskmanager.notification.consumer.message.task.EmailConsumer
import taskmanager.notification.error.NotificationError
import taskmanager.notification.model.message.IMessageRequest
import taskmanager.notification.model.message.Recipient
import taskmanager.scheduler.scheduling.ScheduleType
import kotlin.uuid.Uuid

/**
 * Represents a request to send an Email notification task.
 *
 * @property groupId The group ID of the task.
 * @property replace Whether to replace the task if it already exists.
 * @property description The description of the task.
 * @property scheduleType Optional [ScheduleType] for the task.
 * @property recipients List of target recipients.
 * @property template The template to be used for the notification.
 * @property fields Optional fields to be included in the template.
 * @property cc List of recipients to be copied on the notification.
 * @property subject The subject or title of the notification.
 */
@Serializable
public data class EmailRequest internal constructor(
    override val groupId: Uuid,
    override val description: NoBlankString,
    override val replace: Boolean,
    override val scheduleType: ScheduleType? = null,
    override val recipients: List<Recipient>,
    override val template: NoBlankString,
    override val fields: Map<NoBlankString, NoBlankString>? = null,
    val cc: List<NoBlankString> = emptyList(),
    val subject: NoBlankString,
) : IMessageRequest {

    init {
        verify()
    }

    override fun toMap(taskId: String, recipient: Recipient): MutableMap<String, Any?> {
        return super.toMap(taskId = taskId, recipient = recipient).apply {
            this[EmailConsumer.Property.CC.key] = cc
            this[EmailConsumer.Property.SUBJECT.key] = subject
        }
    }

    internal companion object {
        /**
         * Verifies the recipients of the request.
         *
         * @param request The [EmailRequest] instance to be verified.
         */
        fun verifyRecipients(request: EmailRequest) {
            // This verification of recipients must be done within the scope of a route endpoint,
            // and not in the request dataclass init block, which is actually called before
            // the dataclass reaches the route endpoint scope.
            // This way we can raise a custom error response, as opposed to a generic
            // 400 Bad Request, which would be raised if the validation was done in the init block.
            request.recipients.forEach { recipient ->
                EmailValidator.verify(value = recipient.target).onFailure { error ->
                    throw NotificationError.InvalidEmail(groupId = request.groupId, email = recipient.target, cause = error)
                }
            }
        }
    }
}
