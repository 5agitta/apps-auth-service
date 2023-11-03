package com.sagitta.authservice.restexception.advice;

import com.sagitta.authservice.restexception.exception.RESTException;
import com.sagitta.authservice.restexception.dto.ExceptionResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class RESTExceptionAdvice {
	
	@ExceptionHandler (RESTException.class)
	public ResponseEntity<ExceptionResponseDto> handleRESTException(RESTException exception) {
		var response = ExceptionResponseDto.builder()
		                                   .message(exception.getMessage())
		                                   .errors(exception.getPayload() == null ?
				                                           null : exception.getPayload()
				                                                           .entrySet()
				                                                           .stream()
				                                                           .collect(HashMap::new,
				                                                                    (map, entry) -> map.put(
						                                                                    entry.getKey(),
						                                                                    entry.getValue()
						                                                                         .toString()),
				                                                                    Map::putAll))
		                                   .build();
		return ResponseEntity.status(exception.getStatus())
		                     .body(response);
	}
}
