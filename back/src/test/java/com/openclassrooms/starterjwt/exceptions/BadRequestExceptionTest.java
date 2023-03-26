package com.openclassrooms.starterjwt.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.openclassrooms.starterjwt.exception.BadRequestException;

public class BadRequestExceptionTest {
	@Test
	void testBadRequestException() {
		try {
			throw new BadRequestException();
		} catch (BadRequestException ex) {
			ResponseStatus responseStatus = ex.getClass().getAnnotation(ResponseStatus.class);
			HttpStatus expectedStatus = responseStatus.value();
			assertEquals(HttpStatus.BAD_REQUEST, expectedStatus);
		}
	}
}
