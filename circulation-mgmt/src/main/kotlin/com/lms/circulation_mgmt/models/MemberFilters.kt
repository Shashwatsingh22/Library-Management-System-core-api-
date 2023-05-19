package com.lms.circulation_mgmt.models

import com.lms.commons.constants.Constant

/**
 * @author Shashwat Singh
 * Filters for listing members
 */
class MemberFilters {

    var offset: Int = Constant.DEFAULT_OFFSET
    var limit: Int = Constant.DEFAULT_LIMIT
    var searchText: String? = null
    var collegeName: String? =null
    var statusIds: List<Int>? = null
    var userId: String? = null
    var userName: String? = null
}