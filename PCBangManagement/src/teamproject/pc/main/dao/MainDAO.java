package teamproject.pc.main.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import teamproject.pc.product.db.ConnectionManager;

public class MainDAO {
	ConnectionManager manager = ConnectionManager.getInstance();
	
	public String getTime(String id){
		String time = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;		
		
		conn = manager.getConnection();
		
		String sql = "select start_time from pc_member where pc_member_id = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			rs.next();
			time = rs.getString("start_time");
			System.out.println(time);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			manager.closeConnection(conn, pstmt, rs);
		}
		
		return time;
	}
	
	public int insertPrice(String id, int price){
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;		
		
		conn = manager.getConnection();
		
		String sql = "update pc_member set price = ? where pc_member_id = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, price);
			pstmt.setString(2, id);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			manager.closeConnection(conn, pstmt);
		}		
		
		return result;
	}
	
	public int getPrice(String id){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = 0;
		
		conn = manager.getConnection();
		
		String sql = "select price from pc_member where pc_member_id = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			rs.next();
			result = rs.getInt("price");
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			manager.closeConnection(conn, pstmt, rs);
		}
		
		return result;
	}
	
	public void upload(String id){
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		conn = manager.getConnection();
		
		String sql = "";
	}
}
