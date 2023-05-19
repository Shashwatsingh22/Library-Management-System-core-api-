package com.lms.circulation_mgmt.service

import com.lms.circulation_mgmt.controller.MemberController
import com.lms.commons.constants.ApplicationExceptionTypes
import com.lms.commons.enums.UserStatus
import com.lms.commons.integration.service.PasswordService
import com.lms.commons.models.ApplicationException
import com.lms.commons.models.IdName
import com.lms.commons.models.User
import com.lms.core.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

/**
 * @author Shashwat Singh
 */

@Service
open class MemberControllerService {

    private val log = LoggerFactory.getLogger(MemberController::class.java)

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var passwordService: PasswordService

    open fun onboardMember(member: User, loggedInUser: User) {
        userService.userSignup(member)
        member.password = null
        log.info("onboardMember - Librarian <${loggedInUser.id}> added an new member ${member.id}")
    }

    open fun checkIfMemberExists(memberId: UUID, loggedInUser: User): User {
        val memberData = userService.get(User(memberId.toString()))
        if (memberData == null) {
            log.info("checkIfMemberExists - Librarian <${loggedInUser.id}>, trying to delete a member <${memberId}> that does not exists.")
            throw ApplicationException(ApplicationExceptionTypes.MEMBER_NOT_FOUND)
        }
        return memberData
    }

    open fun deleteMember(memberId: UUID, loggedInUser: User) {
        val member = checkIfMemberExists(memberId, loggedInUser)
        member.status = IdName(UserStatus.DELETED.id)
        member.deletedBy = User(loggedInUser.id!!)

        val deletedRow = userService.update(member)
        if(deletedRow != 1) {
            log.info("deleteMember - Failed to delete member <${member.id}> by librarian <${loggedInUser.id}>")
            throw ApplicationException(ApplicationExceptionTypes.GENERIC_EXCEPTION)
        }
        log.info("deleteMember - Successfully deleted member <${member.id}> by librarian <${loggedInUser.id}>")
    }

    open fun updateMemberDetails(memberId: UUID, updatedMemberDetails: User, loggedInUser: User): User {
        if(!updatedMemberDetails.password.isNullOrEmpty()) {
            updatedMemberDetails.password = passwordService.encryptPassword(updatedMemberDetails.password!!)
        }
        updatedMemberDetails.id = memberId.toString()
        val updatedRow = userService.update(updatedMemberDetails)
        if(updatedRow != 1) {
            log.info("updateMemberDetails - Failed to update member <${memberId}> details by librarian <${loggedInUser.id}>")
            throw ApplicationException(ApplicationExceptionTypes.GENERIC_EXCEPTION)
        }
        log.info("updateMemberDetails - Successfully updated member <${memberId}> details by librarian <${loggedInUser.id}>")
        return userService.get(User(memberId.toString()))!!
    }
}