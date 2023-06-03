package com.lms.commons.dao

import com.lms.commons.models.IdName
import org.apache.ibatis.annotations.Mapper

/**
 * @author Shashwart Singh
 */
@Mapper
interface CommonDao {

    fun getTypes(groupId : Int,typeId : Int ?= null): List<IdName>
}