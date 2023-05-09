package com.lms.commons.services

import com.lms.commons.models.DeviceInfo
import org.springframework.stereotype.Service
import ua_parser.Client
import ua_parser.Parser

/**
 * @author Shashwat Singh
 */

@Service
open class CommonService {

    open fun parseUserAgent(userAgent : String) : DeviceInfo {
        val uaParser = Parser()
        val clientString : Client = uaParser.parse(userAgent)

        val deviceInfo = DeviceInfo()
        deviceInfo.osVersion = "${clientString.os.family} ${clientString.os.major} ${clientString.os.minor}"
        deviceInfo.browser = clientString.userAgent.family
        deviceInfo.browserVersion = "${clientString.userAgent.family} ${clientString.userAgent.major} ${clientString.userAgent.minor}"
        deviceInfo.deviceName = clientString.device.family

        return deviceInfo
    }
}