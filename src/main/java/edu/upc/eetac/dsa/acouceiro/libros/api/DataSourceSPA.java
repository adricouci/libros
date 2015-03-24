package edu.upc.eetac.dsa.acouceiro.libros.api;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
 
public class DataSourceSPA {
    private DataSource dataSource;
	private static DataSourceSPA instance;
 
	private DataSourceSPA() {
		super();
		Context envContext = null;
		try {
			envContext = new InitialContext();
			Context initContext = (Context) envContext.lookup("java:/comp/env");
			dataSource = (DataSource) initContext.lookup("jdbc/librosdb"); // Referencia al DataSource 
		} catch (NamingException e1) {
			e1.printStackTrace();
		}
	}
 
	// Modo de obtener la instancia de Singleton
	public final static DataSourceSPA getInstance() {
		if (instance == null)
			instance = new DataSourceSPA();
		return instance;
	}
	// Metodo para obtener lo que nos interesa = Data Source
	public DataSource getDataSource() {
		return dataSource;
	}
}
