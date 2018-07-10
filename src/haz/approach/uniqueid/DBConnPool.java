package haz.approach.uniqueid;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DBConnPool {

	private static DBConnPool instance;  
	static ComboPooledDataSource cpds = null;
	private static Object syncObject_;
	
	private DBConnPool(){
		cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass( DB_DRIVER );
			cpds.setJdbcUrl( DB_CONNECTION );
			cpds.setUser(DB_USER);
			cpds.setPassword(DB_PASSWORD);
			
			// the settings below are optional -- c3p0 can work with defaults
			cpds.setInitialPoolSize(20);
			cpds.setMinPoolSize(20);
			cpds.setAcquireIncrement(5);
			cpds.setMaxPoolSize(20);
			cpds.setNumHelperThreads(10);
			//cpds.setMaxAdministrativeTaskTime(1000);
		} 
		//catch (PropertyVetoException e) {
		catch (Exception e) {
			e.printStackTrace();
		} //loads the jdbc driver

	}

	public static DBConnPool getInstance() {    
		if (instance==null)  
		{  
			synchronized(DBConnPool.class) {
				if (instance==null) {
					instance=new  DBConnPool();  
				}
			}
		}  
		return instance;  
	}  

	private static final String DB_DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String DB_CONNECTION = "jdbc:oracle:thin:@10.150.113.27:1521:orcl";
	private static final String DB_USER = "system";
	private static final String DB_PASSWORD = "A*******";

	public Connection getDBConnection() throws Exception {
		return cpds.getConnection();
	}
	
	public void closeConnection(){
		try {
			cpds.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
