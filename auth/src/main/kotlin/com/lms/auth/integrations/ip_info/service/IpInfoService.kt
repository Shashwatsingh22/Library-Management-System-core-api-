package com.lms.auth.integrations.ip_info.service

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lms.auth.constants.Constants
import com.lms.auth.integrations.ip_info.models.IpDetails
import com.lms.auth.integrations.ip_info.models.IpInfoEnv
import com.lms.auth.integrations.ip_info.models.IpInfoResponse
import com.lms.commons.utils.http.HttpUtil
import com.lms.commons.utils.toJson
import org.apache.http.entity.ContentType
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service


/**
 * @author Shashwat Singh
 * This class contains methods to interact with IpInfo.
 */

@Service
open class IpInfoService {

	private val log = LoggerFactory.getLogger(IpInfoService::class.java)

	@Autowired
	private lateinit var ipInfoEnv: IpInfoEnv

	open fun getIpInformation(ip : Set<String>) : IpInfoResponse {

		val response = HttpUtil.post(
			url = "${Constants.IpInfo.url}/${Constants.IpInfo.Uri.batch}",
			body = ip,
			headers = mapOf(
				HttpHeaders.CONTENT_TYPE to ContentType.create(
					MediaType.APPLICATION_JSON_VALUE,
					charset(Charsets.UTF_8.name())
				).toString(),
				HttpHeaders.AUTHORIZATION to "Bearer ${ipInfoEnv.token}"
			),
			clazz = String::class.java)

		log.info(
			"getIpInformation - Received response <${response.response?.toJson()}> and status <${response.statusCode}> for IP <$ip>"
		)

		val ipInfoResponse = IpInfoResponse()
		ipInfoResponse.statusCode = response.statusCode

		return if(response.statusCode == HttpStatus.OK.value()) {
			val mapType = object : TypeToken<Map<String, IpDetails?>>() {}.type
			val ipInfoMap: Map<String, IpDetails?> = Gson().fromJson(response.response, mapType)
			ipInfoResponse.ipDetails = ipInfoMap
			ipInfoResponse
		} else {
			ipInfoResponse.errorMessage = response.errorBody
			ipInfoResponse
		}
	}

}
