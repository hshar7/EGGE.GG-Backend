package com.hshar.eggegg.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Resource already exists.")
class DuplicateResourceException(message: String?) : RuntimeException(message)
