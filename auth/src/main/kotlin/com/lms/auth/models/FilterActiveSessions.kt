package com.lms.auth.models

import com.lms.commons.constants.Constant
import com.lms.commons.models.User

/**
 * @author Shashwat Singh
 * Model to filter Active Sessions
 */
class FilterActiveSessions{

    var user: User? = null
    var offset: Int = Constant.DEFAULT_OFFSET
    var limit: Int = Constant.DEFAULT_LIMIT
    var includeToken: Boolean? = null
}