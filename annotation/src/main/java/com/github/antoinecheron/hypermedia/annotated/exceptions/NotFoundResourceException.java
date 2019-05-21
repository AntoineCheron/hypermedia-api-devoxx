package com.github.antoinecheron.hypermedia.annotated.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFoundResourceException extends RuntimeException {}
