package com.hshar.daory.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "User not authorized to perform action.")
class UnauthorizedException(message: String?) : RuntimeException(message)
