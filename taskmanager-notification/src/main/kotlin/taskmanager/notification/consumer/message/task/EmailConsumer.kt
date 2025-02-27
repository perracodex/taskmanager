/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package taskmanager.notification.consumer.message.task

import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.EmailAttachment
import org.apache.commons.mail.HtmlEmail
import taskmanager.base.env.Tracer
import taskmanager.base.settings.AppSettings
import taskmanager.base.settings.catalog.section.CommunicationSettings
import taskmanager.base.util.CastUtils
import taskmanager.notification.consumer.message.AbsNotificationConsumer

/**
 * Represents a scheduled task that processes emails.
 */
internal class EmailConsumer : AbsNotificationConsumer() {
    private val tracer: Tracer = Tracer<EmailConsumer>()

    /**
     * Represents the concrete properties for the email task.
     */
    enum class Property(val key: String) {
        CC(key = "CC"),
        SUBJECT(key = "SUBJECT")
    }

    override fun consume(payload: ConsumerPayload) {
        tracer.debug("Processing email notification. Group Id: ${payload.groupId} | Task Id: ${payload.taskId}")

        val emailSpec: CommunicationSettings.EmailSpec = AppSettings.communication.emailSpec
        verifySettings(spec = emailSpec)

        // Build the message.
        val email: HtmlEmail = HtmlEmail().apply {
            val message: String = buildMessage(
                type = TemplateType.EMAIL,
                payload = payload
            )

            setHtmlMsg(message)
            setTextMsg("Your email client does not support HTML messages.")
        }

        // Add recipients to be copied on the email notification.
        val cc: List<String>? = CastUtils.toStringList(list = payload.additionalParameters[Property.CC.key])
        cc?.forEach { ccAddress ->
            email.addCc(ccAddress)
        }

        // Add attachments to the email.
        payload.attachments?.forEach { attachmentPath ->
            val attachment: EmailAttachment = EmailAttachment().apply {
                path = attachmentPath
                disposition = EmailAttachment.ATTACHMENT
            }

            email.attach(attachment)
        }

        // Configure email settings.

        email.apply {
            hostName = emailSpec.hostName
            setSmtpPort(emailSpec.smtpPort)
            authenticator = DefaultAuthenticator(emailSpec.username, emailSpec.password)
            isSSLOnConnect = emailSpec.isSSLOnConnect
            setFrom(emailSpec.username)
            addTo(payload.recipient.target)
            subject = payload.additionalParameters[Property.SUBJECT.key] as? String
        }

        // Send the email.
        email.send().also { messageId ->
            tracer.debug(
                "Email notification sent: ${payload.recipient.target}. " +
                        "Group Id: ${payload.groupId} " +
                        "| Task Id: ${payload.taskId} " +
                        "| Message Id: $messageId"
            )
        }
    }

    /**
     * Verifies that the email settings values are valid.
     */
    private fun verifySettings(spec: CommunicationSettings.EmailSpec) {
        require(spec.smtpPort > 0) { "Invalid email SMTP port. Got: '${spec.smtpPort}'" }
        require(spec.hostName.isNotBlank()) { "Invalid email host name. Got: '${spec.hostName}'" }
        require(spec.username.isNotBlank()) { "Invalid email username. Got: '${spec.username}'" }
        require(spec.password.isNotBlank()) { "Invalid email password. Got: '${spec.password}'" }
    }
}
