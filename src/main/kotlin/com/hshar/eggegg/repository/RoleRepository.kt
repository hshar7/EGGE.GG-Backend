package com.hshar.eggegg.repository

import com.hshar.eggegg.model.permanent.Role
import com.hshar.eggegg.model.transient.type.RoleName
import org.springframework.data.mongodb.repository.MongoRepository

interface RoleRepository : MongoRepository<Role, String> {
    fun findByName(name: RoleName): Role?
}
