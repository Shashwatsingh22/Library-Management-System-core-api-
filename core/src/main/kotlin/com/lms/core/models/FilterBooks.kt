package com.lms.core.models

import com.lms.commons.constants.Constant

/**
 * @author Shashwat Singh
 */

class FilterBooks {

    var searchText : String? = null
    var offset : Int = Constant.DEFAULT_OFFSET
    var limit : Int = Constant.DEFAULT_LIMIT
    var statusIds : List<Int>? = null
    var genreIds: List<Int>? = null
    var bookId: String? = null
}