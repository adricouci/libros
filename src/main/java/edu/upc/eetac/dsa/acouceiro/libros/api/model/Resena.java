package edu.upc.eetac.dsa.acouceiro.libros.api.model;

import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLink.Style;
import org.glassfish.jersey.linking.InjectLinks;

import edu.upc.eetac.dsa.acouceiro.libros.api.MediaType;
import edu.upc.eetac.dsa.acouceiro.libros.api.ResenaResource;

public class Resena {

	@InjectLinks({
		@InjectLink(resource = ResenaResource.class, style = Style.ABSOLUTE, rel = "opinion", title = "Latest opinion", type = MediaType.LIBRO_API_RESENA_COLLECTION),
		@InjectLink(resource = ResenaResource.class, style = Style.ABSOLUTE, rel = "self edit", title = "Opinion", type = MediaType.LIBRO_API_RESENA, method = "getOpinion", bindings = @Binding(name = "idOpinion", value = "${instance.id}")) })
	
	private List<Link> links;
	private int idresena;
	private int idlibro;
	private String creador;
	private String datos;
	private String fecha;
	private long lastModified;
	
	
	
	public List<Link> getLinks() {
		return links;
	}
	public void setLinks(List<Link> links) {
		this.links = links;
	}
	public long getLastModified() {
		return lastModified;
	}
	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}
	public int getIdresena() {
		return idresena;
	}
	public void setIdresena(int idresena) {
		this.idresena = idresena;
	}
	public int getIdlibro() {
		return idlibro;
	}
	public void setIdlibro(int idlibro) {
		this.idlibro = idlibro;
	}
	public String getCreador() {
		return creador;
	}
	public void setCreador(String creador) {
		this.creador = creador;
	}
	public String getDatos() {
		return datos;
	}
	public void setDatos(String datos) {
		this.datos = datos;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	
	
	
}
