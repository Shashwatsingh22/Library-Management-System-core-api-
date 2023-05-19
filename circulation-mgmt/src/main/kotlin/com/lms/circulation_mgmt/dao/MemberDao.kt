package com.lms.circulation_mgmt.dao

import com.lms.circulation_mgmt.models.MemberFilters
import com.lms.commons.models.Pagination
import com.lms.commons.models.User
import org.apache.ibatis.annotations.Mapper

/**
 * @author Shashwat Singh
 */

@Mapper
interface MemberDao {

    fun getMembers(memberFilters: MemberFilters): Pagination<User>?
}