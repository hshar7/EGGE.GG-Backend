package com.hshar.eggegg.graphql

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.hshar.eggegg.model.Tournament
import com.hshar.eggegg.repository.TournamentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class Query : GraphQLQueryResolver {

    @Autowired
    lateinit var tournamentRepository: TournamentRepository

    fun getTournaments(count: Int, offset: Int): Page<Tournament> {
        val sort = Sort(Sort.Direction.DESC, "createdAt")
        return tournamentRepository.findAll(PageRequest.of(offset, count, sort))
    }
}
