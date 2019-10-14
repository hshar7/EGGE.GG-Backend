package com.hshar.eggegg.repository

import com.hshar.eggegg.model.permanent.redis.Web3Data
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface Web3DataRepository : CrudRepository<Web3Data, String>
