/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package ktask.core.plugins

import io.github.perracodex.kopapi.plugin.Kopapi
import io.github.perracodex.kopapi.type.*
import io.ktor.server.application.*
import ktask.core.settings.AppSettings

/**
 * Configures OpenAPI, Swagger-UI and Redoc.
 *
 * #### References
 * - [Kopapi Documentation](https://github.com/perracodex/kopapi)
 */
public fun Application.configureApiSchema() {

    if (!AppSettings.apiSchema.environments.contains(AppSettings.runtime.environment)) {
        return
    }

    install(plugin = Kopapi) {
        enabled = true

        apiDocs {
            openApiUrl = AppSettings.apiSchema.openApiEndpoint
            openApiFormat = OpenApiFormat.YAML
            redocUrl = AppSettings.apiSchema.redocEndpoint

            swagger {
                url = AppSettings.apiSchema.swaggerEndpoint
                persistAuthorization = true
                withCredentials = false
                docExpansion = SwaggerDocExpansion.NONE
                displayRequestDuration = true
                displayOperationId = true
                operationsSorter = SwaggerOperationsSorter.UNSORTED
                uiTheme = SwaggerUiTheme.DARK
                syntaxTheme = SwaggerSyntaxTheme.NORD
                includeErrors = true
            }
        }

        info {
            title = "Ktask API"
            version = "1.0.0"
            description = "Task API Documentation"
            license {
                name = "MIT"
                url = "https://opensource.org/licenses/MIT"
            }
        }

        servers {
            add(urlString = "{protocol}://{host}:{port}") {
                description = "Ktask API Server"
                variable(name = "protocol", defaultValue = "http") {
                    choices = setOf("http", "https")
                }
                variable(name = "host", defaultValue = "localhost") {
                    choices = setOf(AppSettings.deployment.host, "localhost")
                }
                variable(name = "port", defaultValue = "8080") {
                    choices = setOf(
                        AppSettings.deployment.port.toString(),
                        AppSettings.deployment.sslPort.toString(),
                    )
                }
            }
        }

        tags {
            add(name = "System", description = "System Management")
            add(name = "Scheduler", description = "Scheduler Management")
            add(name = "Scheduler Admin", description = "Scheduler Maintenance")
        }
    }
}
