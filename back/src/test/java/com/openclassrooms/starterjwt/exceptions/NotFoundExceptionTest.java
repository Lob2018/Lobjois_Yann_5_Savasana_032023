package com.openclassrooms.starterjwt.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.openclassrooms.starterjwt.exception.NotFoundException;

public class NotFoundExceptionTest {
	@Test
	void testNotFoundException() {
		try {
			throw new NotFoundException();
		} catch (NotFoundException ex) {
			ResponseStatus responseStatus = ex.getClass().getAnnotation(ResponseStatus.class);
			HttpStatus expectedStatus = responseStatus.value();
			assertEquals(HttpStatus.NOT_FOUND, expectedStatus);
		}
	}
}