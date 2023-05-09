package com.lms.commons.utils.http

import com.lms.commons.utils.http.model.HttpResponse
import org.slf4j.LoggerFactory
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider
import java.time.Duration

/**
 * @author Shashwat Singh
 */

object HttpUtil {

	private var log = LoggerFactory.getLogger(HttpUtil::class.java)
	object ConnectionConfig {
		const val maxConnection = 500
		val acquireTimeout: Duration = Duration.ofSeconds(45)
		val idleDuration: Duration = Duration.ofSeconds(600)
	}

	fun <T> post(
		url: String,
		body: Any?,
		headers: Map<String, String>?,
		clazz: Class<T>? = null,
		typeRef: ParameterizedTypeReference<T>? = null,
		webClient: WebClient? = null
	): HttpResponse<T> {
		val requestHeadersSpec = body?.let { (webClient ?: getWebClient(url)).post().uri(url).bodyValue(it) } ?: getWebClient(url).post().uri(url)
		return executeRequest(headers, requestHeadersSpec, clazz, typeRef)
	}


	/**
	 * Sends a HTTP POST request with URL-Encoded body.
	 */
	fun <T> postUrlEncoded(
		url: String,
		body: MultiValueMap<String, String>,
		headers: Map<String, String>?,
		clazz: Class<T>? = null,
		typeRef: ParameterizedTypeReference<T>? = null
	): HttpResponse<T> {
		val requestHeadersSpec = getWebClient(url).post().uri(url).body(BodyInserters.fromFormData(body))
		val allHeaders = mutableMapOf(HttpHeaders.CONTENT_TYPE to MediaType.APPLICATION_FORM_URLENCODED_VALUE)
		headers?.let {
			allHeaders.putAll(it)
		}
		return executeRequest(allHeaders, requestHeadersSpec, clazz, typeRef)
	}

	fun <T> get(
		url: String,
		headers: Map<String, String>?,
		queryParams: Map<String, String>?,
		clazz: Class<T>? = null,
		typeRef: ParameterizedTypeReference<T>? = null): HttpResponse<T> {
		val requestHeadersSpec = getWebClient(url).get().uri { uriBuilder ->
			queryParams?.let { qp ->
				// to be removed when spring fixes the issue of encoding + in query params
				uriBuilder.apply {
					qp.keys.forEach { key -> queryParam(key, "{$key}") } }.build(qp) }
			}
		return executeRequest(headers, requestHeadersSpec, clazz, typeRef)
	}

	private fun <T> executeRequest(
		headers: Map<String, String>?, requestHeadersSpec: WebClient.RequestHeadersSpec<*>,
		clazz: Class<T>?, typeRef: ParameterizedTypeReference<T>?): HttpResponse<T> {
		headers?.forEach {
			requestHeadersSpec.header(it.key, it.value)
		}
		return try {
			val entity = clazz?.let {
				requestHeadersSpec.retrieve().toEntity(clazz)
			} ?: requestHeadersSpec.retrieve().toEntity(typeRef!!)
			val block = entity.block()
			HttpResponse(block?.statusCode?.value(), block?.body)
		} catch (e: WebClientResponseException) {
			if (e.rawStatusCode == HttpStatus.OK.value()) {
				HttpResponse(e.rawStatusCode, null, null, e)
			} else {
				HttpResponse(e.rawStatusCode, null, e.responseBodyAsString, e)
			}
		} catch (e: Exception) {
			log.error("executeRequest - Error Occurred: ${e.message}")
			HttpResponse(null, null, null, e)
		}
	}

	var getWebClient: (String) -> WebClient = {

		// To Fix Connection reset by peer error
		// https://github.com/reactor/reactor-netty/issues/1774#issuecomment-905774007
		val provider = ConnectionProvider.builder("provider")
			.maxConnections(ConnectionConfig.maxConnection)
			.pendingAcquireTimeout(ConnectionConfig.acquireTimeout)
			.maxIdleTime(ConnectionConfig.idleDuration).build()

		WebClient.builder()
			.clientConnector(ReactorClientHttpConnector(HttpClient.create(provider)))
			.baseUrl(it)
			.build()
	}
}
