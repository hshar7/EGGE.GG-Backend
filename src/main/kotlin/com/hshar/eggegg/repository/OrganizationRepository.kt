package com.hshar.eggegg.repository

import com.hshar.eggegg.model.Organization
import org.springframework.data.mongodb.repository.MongoRepository

interface OrganizationRepository : MongoRepository<Organization, String>
