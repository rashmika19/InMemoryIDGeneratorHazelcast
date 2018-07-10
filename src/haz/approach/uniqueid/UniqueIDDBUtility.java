package haz.approach.uniqueid;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import haz.approach.uniqueid.DBConnPool;
import java.io.Serializable;
public class UniqueIDDBUtility implements Serializable {
	private static Connection getDBConnection() throws Exception {

		DBConnPool pool = DBConnPool.getInstance();
		return pool.getDBConnection();

	}
	

	public Long selectRow(String ins) throws SQLException{
		String sql = "select uniqueid from uniquekey where pzinskey = ?";
		Connection dbConnection = null;
		PreparedStatement  statement = null;
		ResultSet rs = null;
		try{
			dbConnection = getDBConnection();
			statement = dbConnection.prepareStatement(sql);
			statement.setString(1, ins);

			rs = statement.executeQuery();
			if(rs.next()){
				Long curr = rs.getLong("UNIQUEID");
				return curr; 
			}
		}catch (Exception e) {

			System.out.println(e.getMessage());

		} finally {
			if (rs != null) {
				rs.close();
			}
			if (statement != null) {
				statement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}

		}
		return null;
	}

	public void updateRow(String ins, Long val) throws SQLException{
		String sql = "update uniquekey set uniqueid = ? where pzinskey = ? and uniqueid < ?";
		Connection dbConnection = null;
		PreparedStatement  statement = null;

		try{
			dbConnection = getDBConnection();
			statement = dbConnection.prepareStatement(sql);
			statement.setString(2, ins);
			statement.setFloat(1, val);
			statement.setFloat(3, val);

			statement.executeQuery();
		}catch (Exception e) {

			System.out.println(e.getMessage());

		} finally {

			if (statement != null) {
				statement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}

		}
	}

	public void insertRow(String ins, Long val) throws Exception {

		Connection dbConnection = null;
		PreparedStatement  statement = null;

		String insertTableSQL = "INSERT INTO uniquekey"
				+ "(PZINSKEY, UNIQUEID) " + "VALUES (?,?)";


		try {
			dbConnection = getDBConnection();
			statement = dbConnection.prepareStatement(insertTableSQL);
			statement.setString(1, ins);
			statement.setFloat(2, val);
			statement.executeQuery();



		} finally {

			if (statement != null) {
				statement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}

		}

	}
}
