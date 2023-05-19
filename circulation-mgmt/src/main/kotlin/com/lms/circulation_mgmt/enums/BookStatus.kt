package com.lms.circulation_mgmt.enums

/**
 * @author Shashwat Singh
 * BookStatus (GroupId - 2)
 */
enum class BookStatus(val id: Int) {
    AVAILABLE(3), BORROWED(4), REMOVED(5)
}