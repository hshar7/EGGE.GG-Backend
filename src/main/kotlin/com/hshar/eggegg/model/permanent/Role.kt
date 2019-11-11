package com.hshar.eggegg.model.permanent

import com.hshar.eggegg.model.transient.type.RoleName
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "roles")
data class Role (
        val id: String,
        var name: RoleName
)
