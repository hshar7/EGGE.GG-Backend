package com.hshar.eggegg.config

import com.hshar.eggegg.model.permanent.Role
import com.hshar.eggegg.model.transient.type.RoleName
import com.hshar.eggegg.repository.RoleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
@ConfigurationProperties(prefix = "initiate")
class InitiationConfiguration @Autowired constructor(
        private val roleRepository: RoleRepository,
        @Value("\${initiate.roles}") val initiateRoles: String
) {

    init {
        if (initiateRoles == "true") {
            roleRepository.save(Role(
                    id = UUID.randomUUID().toString(),
                    name = RoleName.ROLE_ADMIN
            ))
            roleRepository.save(Role(
                    id = UUID.randomUUID().toString(),
                    name = RoleName.ROLE_ORGANIZER
            ))
            roleRepository.save(Role(
                    id = UUID.randomUUID().toString(),
                    name = RoleName.ROLE_PLAYER
            ))
        }
    }
}
