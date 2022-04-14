package com.based.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {
	@Override
	public Response toResponse(RuntimeException e) {
		return Response.status(400)
				.entity("Error: " + e.getMessage() + "\n")
				.type(MediaType.TEXT_PLAIN)
				.build();
	}
}
