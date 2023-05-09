package com.lms.app.config.web

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.networknt.schema.JsonSchema
import com.networknt.schema.JsonSchemaFactory
import com.networknt.schema.SpecVersion
import com.networknt.schema.ValidationMessage
import com.lms.commons.constants.ApplicationExceptionTypes
import com.lms.commons.constants.Constant
import com.lms.commons.models.ApplicationException
import com.lms.commons.utils.getRequestBody
import io.swagger.v3.oas.annotations.parameters.RequestBody
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Service
import org.springframework.util.FileCopyUtils
import org.springframework.util.StreamUtils
import org.springframework.web.method.HandlerMethod
import java.io.BufferedInputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author Shashwat Singh
 * It contains commons methods which are required at the PostHandle or PreHandling the request/response.
 */

@Service
class CommonWebConfigServices {

    private var log = LoggerFactory.getLogger(CommonWebConfigServices::class.java)

    @Autowired
    private lateinit var resourceLoader: ResourceLoader


    fun validateIncomingRequestBody(request: HttpServletRequest, handler: Any) {
        (handler as? HandlerMethod)?.let {
            val annotation = it.getMethodAnnotation(RequestBody::class.java)
            if (!annotation?.content?.firstOrNull()?.schema?.ref.isNullOrEmpty()) {

                //Fetch the JSON-Schema for particular Request
                val schemaPath = annotation?.content?.get(0)?.schema?.ref!!
                val resource = try {
                    resourceLoader.getResource(Constant.SCHEMA_LOCATION.format(Constant.REQUEST_SCHEMA, schemaPath))
                } catch (e: NullPointerException) {
                    log.error("validateIncomingRequestBody - Request <${request.requestURL}>, not able to find jsonSchema")
                    return
                }

                val reader: Reader = InputStreamReader(resource.inputStream)
                val fileData: String = FileCopyUtils.copyToString(reader)
                val schema: JsonSchema = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4).getSchema(fileData)
                val requestBody = request.getRequestBody()!!
                validateSchema(requestBody, schema)
            }
        }
    }

    private fun validateSchema(requestBody: JsonNode, schema: JsonSchema) {
        //Validating Schema
        val errors: Set<ValidationMessage> = schema.validate(requestBody)
        if (errors.isNotEmpty()) {
            val errorCombined = errors.joinToString("\n") { it.toString() }
            log.info("validateSchema - Found Schema Validation Error. $errorCombined")
            throw ApplicationException(ApplicationExceptionTypes.JSON_SCHEMA_VALIDATION_ERROR, details = errors)
        }
    }

    fun setRequestBody(request: HttpServletRequest) {
        val inputStream: InputStream = BufferedInputStream(request.inputStream)
        val inputStreamBytes: ByteArray = StreamUtils.copyToByteArray(inputStream)
        val requestBody = ObjectMapper().readTree(inputStreamBytes)
        request.setAttribute(Constant.servletAttributeRequestBodyKey, requestBody)
    }

    /**
     * Method to set Security Headers for the API
     * Please refer <a href="https://cheatsheetseries.owasp.org/cheatsheets/REST_Security_Cheat_Sheet.html#security-headers">
     *   OWASP Rest API Cheat Sheet</a>
     */
    fun setSecurityHeaders(response: HttpServletResponse) {
        // Set Security Headers
        response.setHeader("Cache-Control", "no-store")
        response.setHeader("Content-Security-Policy", "frame-ancestors 'none'")
        response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains; preload")
        response.setHeader("X-Content-Type-Options", "nosniff")
        response.setHeader("X-Frame-Options", "DENY")
        response.setHeader("Feature-Policy", "none")
        response.setHeader("Referrer-Policy", "no-referrer")
    }
}