package edu.upc.eetac.dsa.acouceiro.libros.api;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import edu.upc.eetac.dsa.acouceiro.libros.api.model.LibrosError;


public class WebApplicationExceptionMapper implements
ExceptionMapper<WebApplicationException>  {

	@Override
	public Response toResponse(WebApplicationException exception) {
		LibrosError error = new LibrosError(
				exception.getResponse().getStatus(), exception.getMessage());
		return Response.status(error.getStatus()).entity(error)
				.type(MediaType.LIBRO_API_ERROR).build();
	}
	
	
}
