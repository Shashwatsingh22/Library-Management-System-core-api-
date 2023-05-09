package com.lms.auth.integrations.ip_info.models

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @author Shashwat Singh
 * For mapping the api response of ip-info
 */

open class IpInfoResponse {

	var statusCode: Int? = null
	var errorMessage: String? = null
	var ipDetails: Map<String, IpDetails?>? = null
}

data class IpDetails(
	val ip: String,
	val city: String,
	val region: String,
	val country: String,

	@JsonProperty("loc")
	val location: String
)
