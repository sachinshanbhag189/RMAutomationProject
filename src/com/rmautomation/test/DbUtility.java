package com.rmautomation.test;

import static com.rmautomation.test.DriverScript.APP_LOGS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;


//Static Imports
//import static com.rmautomation.test.DriverScript.APP_LOGS;
//import static com.rmautomation.test.DriverScript.CONFIG;
//import static com.rmautomation.test.DriverScript.OR;

public class DbUtility {
	Connection connection = null;
	String url =  "jdbc:oracle:thin:@us000975:1521:dev2";
	String username ="rm";
	String password ="rm";
	String odbc_driver = "oracle.jdbc.driver.OracleDriver";

	public DbUtility(){

		APP_LOGS.debug("-------- Oracle JDBC Connection . Connecting to Oracle database------");
		try 
		{
			Class.forName("oracle.jdbc.driver.OracleDriver"); // Oracle JDBC Driver Registered!

			connection = DriverManager.getConnection(url, username,password);

			if (connection != null) 
			{
				APP_LOGS.debug("Connection to the Database was successful");
			}
			else {
				APP_LOGS.debug("Failed to Connect to the Database!");
			}
		}catch (ClassNotFoundException e) {
			APP_LOGS.debug("java.lang.ClassNotFoundException: oracle.jdbc.driver.OracleDriver./nYou must be missing the odbc jar file in the classpath"+e.getMessage());
			return;

		} catch (SQLException e) {
			APP_LOGS.debug("SQL Exception occured"+e.getMessage());
			return;
		}
	}

	//	public void run_simple_query(String query) throws SQLException{
	//		//connectDatabase();
	//		Statement stmt = connection.createStatement();
	//		ResultSet rs = stmt.executeQuery(query);
	//		ResultSetMetaData rsmd = rs.getMetaData();
	//		System.out.println("Number of columns in the table = "+rsmd.getColumnCount());
	//		System.out.println();
	//
	//		for(int i=1;i<=rsmd.getColumnCount();i++) //Since its 1 based
	//		{
	//			System.out.print(rsmd.getColumnName(i) + " | ");
	//		} 
	//
	//		System.out.println();
	//		int count_result=0;
	//
	//		while(rs.next()){
	//			int total_columns = rsmd.getColumnCount();  //returns  total number of columns  - 1 based
	//
	//			for(int i=1;i<=total_columns;i++) //Since its 1 based
	//			{
	//				System.out.print(rs.getString(i) + " | ");
	//			} 
	//			System.out.println();
	//			count_result++;
	//		}
	//		System.out.println("The query returned "+count_result+" rows");
	//		rs.close();
	//		stmt.close();
	//		//closeDatabase();
	//	} 


	public int get_count_query(String query) throws SQLException{

		Statement stmt=null;
		ResultSet rs= null;
		try{

			int count_result = 0;
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);

			while(rs.next()){
				count_result = rs.getInt(1);  // First Column index is always =1
			}

			return count_result;
		}
		catch(Exception e){
			return 1;
		}
		finally{
			rs.close();
			stmt.close();
		}
	}

	public int run_simple_prepared_query_for_count(String query,String data_value) throws SQLException{
		APP_LOGS.debug("Running the Prepared Statement");
		PreparedStatement pstmt=null;
		ResultSet rs= null;
		int count=0;
		try{
			pstmt = connection.prepareStatement(query);
			pstmt.setString(1, data_value);
			rs = pstmt.executeQuery();			
			if (rs.next()){
				count = rs.getInt(1);
			}
			return count;
		}
		catch(Exception e){
			APP_LOGS.debug("Failed to execute the prepared Statement query."+e.getMessage());
			return -1;
		}
		finally{
			rs.close();
			pstmt.close();
		}
	} 


	public void closeDatabase(){
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
