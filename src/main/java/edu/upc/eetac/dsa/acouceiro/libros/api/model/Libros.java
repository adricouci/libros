package edu.upc.eetac.dsa.acouceiro.libros.api.model;

public class Libros {
	
	private int idlibros;
	private String titulo;
	private String autor;
	private String lengua;
	private String edicion;
	private String fechaedicion;
	private String fechaimpresion;
	private String editorial;
	private long lastmodified;
	
	public long getLastmodified() {
		return lastmodified;
	}
	public void setLastmodified(long lastmodified) {
		this.lastmodified = lastmodified;
	}
	public int getIdlibros() {
		return idlibros;
	}
	public void setIdlibros(int idlibros) {
		this.idlibros = idlibros;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getAutor() {
		return autor;
	}
	public void setAutor(String autor) {
		this.autor = autor;
	}
	public String getLengua() {
		return lengua;
	}
	public void setLengua(String lengua) {
		this.lengua = lengua;
	}
	public String getEdicion() {
		return edicion;
	}
	public void setEdicion(String edicion) {
		this.edicion = edicion;
	}
	public String getFechaedicion() {
		return fechaedicion;
	}
	public void setFechaedicion(String fechaedicion) {
		this.fechaedicion = fechaedicion;
	}
	public String getFechaimpresion() {
		return fechaimpresion;
	}
	public void setFechaimpresion(String fechaimpresion) {
		this.fechaimpresion = fechaimpresion;
	}
	public String getEditorial() {
		return editorial;
	}
	public void setEditorial(String editorial) {
		this.editorial = editorial;
	}
	
	
	
}
