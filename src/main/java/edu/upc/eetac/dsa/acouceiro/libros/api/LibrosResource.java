package edu.upc.eetac.dsa.acouceiro.libros.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import javax.sql.DataSource;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import edu.upc.eetac.dsa.acouceiro.libros.api.model.Libros;
import edu.upc.eetac.dsa.acouceiro.libros.api.model.LibrosCollection;


@Path("/libros")
public class LibrosResource {
	//Referencia del CRUD
	private DataSource ds = DataSourceSPA.getInstance().getDataSource();
	
	@Context
	private SecurityContext security;

	
	private String GET_LIBROS_BY_ID_QUERY = "select * from libros where idlibro=?";
	
	private Libros getLibroFromDatabase(String idlibros) {
		Libros libro = new Libros();
		Connection conn = null;
	
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(GET_LIBROS_BY_ID_QUERY);
			stmt.setInt(1, Integer.valueOf(idlibros));
			System.out.print ("en5"+ GET_LIBROS_BY_ID_QUERY);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				libro.setIdlibros(rs.getInt("idlibro"));
				libro.setTitulo(rs.getString("titulo"));
				libro.setAutor(rs.getString("autor"));
				libro.setLengua(rs.getString("lengua"));
				libro.setEdicion(rs.getString("edicion"));
				libro.setFechaedicion(rs.getString("fechaedicion"));
				libro.setFechaimpresion(rs.getString("fechaimpresion"));
				libro.setEditorial(rs.getString("editorial"));
				libro.setLastmodified(rs.getTimestamp("lastmodified").getTime());
			} 
			else {
				throw new NotFoundException("There's no libro with idlibro="
						+ idlibros);
			}

		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return libro;
	}
	
	@GET
	@Path("/{idlibros}")
	@Produces(MediaType.LIBRO_API_LIBROS)
	public Response getlibro(@PathParam("idlibros") String idlibros,
			@Context Request request) {
		// Create CacheControl
		
		CacheControl cc = new CacheControl();
		Libros libros = getLibroFromDatabase(idlibros);	
		// Calculate the ETag on last modified date of user resource
		EntityTag eTag = new EntityTag(Long.toString(libros.getLastmodified()));
		System.out.print ("en9");
		System.out.println(idlibros);
		// Verify if it matched with etag available in http request
		Response.ResponseBuilder rb = request.evaluatePreconditions(eTag);

		// If ETag matches the rb will be non-null;
		// Use the rb to return the response without any further processing
		if (rb != null) {
			return rb.cacheControl(cc).tag(eTag).build();
		}

		// If rb is null then either it is first time request; or resource is
		// modified
		// Get the updated representation and return with Etag attached to it
		rb = Response.ok(libros).cacheControl(cc).tag(eTag);
		
		return rb.build();
	}
	
	private String GET_LIBROS_BY_AUTOR_FROM_LAST = "select l.* from libros l where autor LIKE ? and l.lastmodified > ? order by lastmodified";
	private String GET_LIBROS_BY_AUTOR = "select l.* from libros l where autor LIKE ? and l.lastmodified < ifnull ( ?, now()) order by lastmodified desc limit ?";

	@GET
	@Path("/search/autor/{nombreautor}")
	@Produces(MediaType.LIBRO_API_LIBROS_COLLECTION)
	public LibrosCollection getLibros(
			@PathParam("nombreautor") String nombreautor,
			@QueryParam("length") int length,
			@QueryParam("before") long before, @QueryParam("after") long after) {
		LibrosCollection coleccionlibros = new LibrosCollection();
		coleccionlibros.setPattern(nombreautor);

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
		PreparedStatement stmt = null;
		try {
			boolean updateFromLast = after > 0;
			stmt = updateFromLast ? conn
					.prepareStatement(GET_LIBROS_BY_AUTOR_FROM_LAST) : conn
					.prepareStatement(GET_LIBROS_BY_AUTOR);
			stmt.setString(1, '%' + nombreautor + '%');

			if (updateFromLast) {
				stmt.setTimestamp(2, new Timestamp(after));

			} else {
				if (before > 0) {
					stmt.setTimestamp(2, new Timestamp(before));

				} else
					stmt.setTimestamp(2, null);
				length = (length <= 0) ? 5 : length;
				stmt.setInt(3, length);
			}

			ResultSet rs = stmt.executeQuery();

			boolean first = true;
			long oldestTimestamp = 0;
			while (rs.next()) {
				Libros libro = new Libros();
				libro.setIdlibros(rs.getInt("idlibro"));
				libro.setTitulo(rs.getString("titulo"));
				libro.setAutor(rs.getString("autor"));
				libro.setLengua(rs.getString("lengua"));
				libro.setEdicion(rs.getString("edicion"));
				libro.setFechaedicion(rs.getString("fechaedicion"));
				libro.setFechaimpresion(rs.getString("fechaimpresion"));
				libro.setEditorial(rs.getString("editorial"));
				libro.setLastmodified(rs.getTimestamp("lastmodified").getTime());
				oldestTimestamp = rs.getTimestamp("lastmodified").getTime();
				libro.setLastmodified(oldestTimestamp);
				if (first) {
					first = false;
					coleccionlibros.setNewestTimestamp(libro.getLastmodified());
				}
				coleccionlibros.addLibros(libro);
			}
			coleccionlibros.setOldestTimestamp(oldestTimestamp);

		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return coleccionlibros;
	}
	
	private String GET_LIBROS_BY_TITULO_FROM_LAST = "select l.* from libros l where titulo LIKE ? and l.lastmodified > ? order by lastmodified";
	private String GET_LIBROS_BY_TITULO = "select l.* from libros l where titulo LIKE ? and l.lastmodified < ifnull ( ?, now()) order by lastmodified desc limit ?";

	@GET
	@Path("/search/titulo/{titulo}")
	@Produces(MediaType.LIBRO_API_LIBROS_COLLECTION)
	public LibrosCollection getLibrostitulo(@PathParam("titulo") String titulo,
			@QueryParam("length") int length,
			@QueryParam("before") long before, @QueryParam("after") long after) {
		LibrosCollection coleccionlibros = new LibrosCollection();
		coleccionlibros.setPattern(titulo);

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
		PreparedStatement stmt = null;
		try {
			boolean updateFromLast = after > 0;
			stmt = updateFromLast ? conn
					.prepareStatement(GET_LIBROS_BY_TITULO_FROM_LAST) : conn
					.prepareStatement(GET_LIBROS_BY_TITULO);
			stmt.setString(1, '%' + titulo + '%');

			if (updateFromLast) {
				stmt.setTimestamp(2, new Timestamp(after));

			} else {
				if (before > 0) {
					stmt.setTimestamp(2, new Timestamp(before));

				} else
					stmt.setTimestamp(2, null);
				length = (length <= 0) ? 5 : length;
				stmt.setInt(3, length);
			}

			ResultSet rs = stmt.executeQuery();

			boolean first = true;
			long oldestTimestamp = 0;
			while (rs.next()) {
				Libros libro = new Libros();
				libro.setIdlibros(rs.getInt("idlibro"));
				libro.setTitulo(rs.getString("titulo"));
				libro.setAutor(rs.getString("autor"));
				libro.setLengua(rs.getString("lengua"));
				libro.setEdicion(rs.getString("edicion"));
				libro.setFechaedicion(rs.getString("fechaedicion"));
				libro.setFechaimpresion(rs.getString("fechaimpresion"));
				libro.setEditorial(rs.getString("editorial"));
				libro.setLastmodified(rs.getTimestamp("lastmodified").getTime());
				oldestTimestamp = rs.getTimestamp("lastmodified").getTime();
				libro.setLastmodified(oldestTimestamp);
				if (first) {
					first = false;
					coleccionlibros.setNewestTimestamp(libro.getLastmodified());
				}
				coleccionlibros.addLibros(libro);
			}
			coleccionlibros.setOldestTimestamp(oldestTimestamp);

		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return coleccionlibros;
	}
	
	private void validateLibro(Libros libro) {
		System.out.println(security.getUserPrincipal().getName());
		if (!security.getUserPrincipal().getName().equals("admin")) {
			throw new ForbiddenException("No eres el Admin, no puedes añadir libros");
			}
		if (libro.getTitulo() == null)
			throw new BadRequestException("Titulo no puede ser nulo.");
		if (libro.getAutor() == null)
			throw new BadRequestException("Autor no puede ser nulo.");
	}
	
	
	
	private String INSERT_LIBRO_QUERY="insert into libros (titulo, autor, lengua, edicion, editorial, fechaedicion, fechaimpresion) values (?,?,?,?,?,?,?)"; 
	
	@POST
	@Consumes(MediaType.LIBRO_API_LIBROS)
	@Produces(MediaType.LIBRO_API_LIBROS)
	public Libros createLibros(Libros libro) {
		validateLibro(libro);
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(INSERT_LIBRO_QUERY,
					Statement.RETURN_GENERATED_KEYS);

			//stmt.setString(1, security.getUserPrincipal().getName());
			stmt.setString(1, libro.getTitulo());
			stmt.setString(2, libro.getAutor());
			stmt.setString(3, libro.getLengua());
			stmt.setString(4, libro.getEdicion());
			stmt.setString(5, libro.getEditorial());
			stmt.setString(6, libro.getFechaedicion());
			stmt.setString(7, libro.getFechaimpresion());
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				int idlibro = rs.getInt(1);

				libro = getLibroFromDatabase(Integer.toString(idlibro));
			} else {
				// Something has failed...
			}
		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return libro;
	}

	private String DELETE_LIBRO_QUERY="DELETE  FROM libros where idlibro=?";
	
	@DELETE
	@Path("/{idlibro}")
	public void deletelibros(@PathParam("idlibro") String idlibro) {
		System.out.println(security.getUserPrincipal().getName());
		if (!security.getUserPrincipal().getName().equals("admin")) {
			throw new ForbiddenException("No eres el Admin, no puedes borrar libros");
			}
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(DELETE_LIBRO_QUERY);
			stmt.setInt(1, Integer.valueOf(idlibro));

			int rows = stmt.executeUpdate();
			if (rows == 0)
				throw new NotFoundException("There's no sting with stingid="
						+ idlibro);// Deleting inexistent sting
		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}
	}
	
	private String UPDATE_LIBRO_QUERY = "update libros set titulo=ifnull(?, titulo), autor=ifnull(?, autor), lengua=ifnull(?, lengua), edicion=ifnull(?, edicion), fechaedicion=ifnull(?, fechaedicion), fechaimpresion=ifnull(?, fechaimpresion), editorial=ifnull(?, editorial) where idlibro=?";
	
	
	@PUT
	@Path("/{idlibro}")
	@Consumes(MediaType.LIBRO_API_LIBROS)
	@Produces(MediaType.LIBRO_API_LIBROS)
	public Libros updateSting(@PathParam("idlibro") String idlibro, Libros libro) {
		System.out.println(security.getUserPrincipal());
		if (!security.isUserInRole("administrador")) {
			throw new ForbiddenException("No eres el Admin, no puedes actualizar libros");
			}
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(UPDATE_LIBRO_QUERY);
			stmt.setString(1, libro.getTitulo());
			stmt.setString(2, libro.getAutor());
			stmt.setString(3, libro.getLengua());
			stmt.setString(4, libro.getEdicion());
			stmt.setString(5, libro.getFechaedicion());
			stmt.setString(6, libro.getFechaimpresion());
			stmt.setString(7, libro.getEditorial());
			stmt.setInt(8, Integer.valueOf(idlibro));
			int rows = stmt.executeUpdate();
			if (rows == 1)
				libro = getLibroFromDatabase(idlibro);
			else {
				throw new NotFoundException("There's no sting with stingid="
						+ idlibro);
			}

		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return libro;
	}
	
}
