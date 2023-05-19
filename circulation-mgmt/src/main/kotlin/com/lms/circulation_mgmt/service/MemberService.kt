package com.lms.circulation_mgmt.service

import com.lms.circulation_mgmt.dao.MemberDao
import com.lms.circulation_mgmt.models.MemberFilters
import com.lms.commons.models.Pagination
import com.lms.commons.models.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author Shashwat Singh
 */

@Service
open class MemberService {

    @Autowired
    private lateinit var memberDao: MemberDao

    open fun getMembers(memberFilters: MemberFilters): Pagination<User>? {
        return memberDao.getMembers(memberFilters)
    }

}