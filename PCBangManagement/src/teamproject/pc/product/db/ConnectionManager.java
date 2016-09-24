package teamproject.pc.product.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionManager {
	private static ConnectionManager instance;	// ���ӿ� ��ü
	private String host = "jdbc:oracle:thin:@70.12.109.61:1521";
	
	private String id = "admin";
	private String pwd = "admin";
	
	private ConnectionManager(){	// ������ private		
	}
	
	public static ConnectionManager getInstance(){	// ��ü ������ �޼���.
		if(instance == null){
			instance = new ConnectionManager();
		}
		return instance;
	}
	
	public Connection getConnection(){
		Connection conn = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(host, id, pwd);			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public void closeConnection(Connection conn){	// Connection�� �ݱ�.
		if(conn != null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void closeConnection(Connection conn, PreparedStatement pstmt){	// Connection, PreparedStatement �ݱ�.
		if(pstmt != null){
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(conn != null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}		
	}
	
	public void closeConnection(Connection conn, PreparedStatement pstmt, ResultSet rs){	// ���� �ݱ�.
		if(rs != null){
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(pstmt != null){
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(conn != null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}		
	}
	
}//class
