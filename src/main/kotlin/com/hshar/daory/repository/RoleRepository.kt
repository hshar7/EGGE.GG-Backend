package com.hshar.daory.repository

import com.hshar.daory.model.permanent.Role
import com.hshar.daory.model.transient.type.RoleName
import org.springframework.data.mongodb.repository.MongoRepository

interface RoleRepository : MongoRepository<Role, String> {
    fun findByName(name: RoleName): Role?
}
