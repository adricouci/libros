package edu.upc.eetac.dsa.acouceiro.libros.api;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class ThrowableMapper implements ExceptionMapper<Throwable>{

	@Override
	public Response toResponse(Throwable exception) {
		throw new WebApplicationException(exception, Response.Status.INTERNAL_SERVER_ERROR);
	}
}
