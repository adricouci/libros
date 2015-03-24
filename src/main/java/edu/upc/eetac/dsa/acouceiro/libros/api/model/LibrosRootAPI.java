package edu.upc.eetac.dsa.acouceiro.libros.api.model;

import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.glassfish.jersey.linking.InjectLink.Style;

import edu.upc.eetac.dsa.acouceiro.libros.api.LibrosResource;
import edu.upc.eetac.dsa.acouceiro.libros.api.LibrosRootAPIResource;
import edu.upc.eetac.dsa.acouceiro.libros.api.MediaType;

public class LibrosRootAPI {
	@InjectLinks({
		@InjectLink(resource = LibrosRootAPIResource.class, style = Style.ABSOLUTE, rel = "self bookmark home", title = "Libros Root API", method = "getRootAPI"),
		@InjectLink(resource = LibrosResource.class, style = Style.ABSOLUTE, rel = "libros", title = "Latest libros", type = MediaType.LIBRO_API_LIBROS_COLLECTION),
		@InjectLink(resource = LibrosResource.class, style = Style.ABSOLUTE, rel = "create-libros", title = "Latest libros", type = MediaType.LIBRO_API_LIBROS) })
	
	private List<Link> links;
 
	public List<Link> getLinks() {
		return links;
	}
 
	public void setLinks(List<Link> links) {
		this.links = links;
	}
	
	
	
}
