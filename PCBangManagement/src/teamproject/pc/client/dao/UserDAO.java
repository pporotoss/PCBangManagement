package teamproject.pc.client.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import teamproject.pc.client.db.ConnectionManager;
import teamproject.pc.client.dto.UserDTO;

public class UserDAO {
	
	ConnectionManager manager=ConnectionManager.getInstane();
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	ArrayList<String> clientContentList=new ArrayList<String>();
	
	public int insert(UserDTO dto) {
		int result = 0;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = manager.getConnection();
			StringBuffer sql = new StringBuffer("insert into pc_member(pc_member_id,pwd,name,phone,price) ");
			sql.append("values(?,?,?,?,?)");

			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, dto.getId());
			pstmt.setString(2, dto.getPwd());
			pstmt.setString(3, dto.getName());
			pstmt.setString(4, dto.getPhone());
			pstmt.setInt(5, dto.getPrice());

			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			result=2; //아이디중복
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			manager.CloseConnection(con, pstmt);
		}
		return result;
	}

	public int checkLogin(UserDTO dto) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = manager.getConnection();
			StringBuffer sql = new StringBuffer("select * from pc_member");
			pstmt=con.prepareStatement(sql.toString());
			rs=pstmt.executeQuery();
			String id=dto.getId();
			String pwd=dto.getPwd();
			while(rs.next()){
				if(rs.getString("pc_member_id").equals(id) && rs.getString("pwd").equals(pwd)){
					return 1;
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally {
			manager.CloseConnection(con, pstmt,rs);
		}
		return 0;
	}
	
	public ArrayList<String> getSubContent(UserDTO dto){
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con=manager.getConnection();
			String sql="select * from pc_member where pc_member_id=? and pwd=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, dto.getId()); //index 1번부터 시작!!!!!
			pstmt.setString(2, dto.getPwd());
			rs=pstmt.executeQuery();
			rs.next();
			
			String id=rs.getString("pc_member_id");
			String start_time=dto.getStartTime();
			String price=Integer.toString(rs.getInt("price"));
			
			clientContentList.add(id);
			clientContentList.add(start_time);
			clientContentList.add(price);
			
			return clientContentList;
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			manager.CloseConnection(con, pstmt, rs);
		}
		return null;
	}
	//초기화
	public int priceUpdate(String id,int price,String start_time,String connection){ 
		int result=0;
		try {
			con=manager.getConnection();
			String sql="update pc_member set price=?, start_time=?, connection=? where pc_member_id=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, price); 
			pstmt.setString(2, start_time);
			pstmt.setString(3, connection);
			pstmt.setString(4, id);
			result=pstmt.executeUpdate();
			
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			manager.CloseConnection(con, pstmt);
		}
		return result;
	}
	
	// 가격 업데이트
	public int priceUpdate(String id,int price){ 
		int result=0;
		try {
			con=manager.getConnection();
			String sql="update pc_member set price=? where pc_member_id=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, price);			
			pstmt.setString(2, id);
			result=pstmt.executeUpdate();
			
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			manager.CloseConnection(con, pstmt);
		}
		return result;
	}
	
	public int getPrice(String id){
		int result = 0;
		
		con = manager.getConnection();		
		String sql = "select price from pc_member where pc_member_id=?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			rs.next();
			result = rs.getInt("price");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	public String getConnection(String id){
		String result= null;
		try {
			con = manager.getConnection();
			StringBuffer sql = new StringBuffer("select connection from pc_member where pc_member_id=?");
			pstmt=con.prepareStatement(sql.toString());
			pstmt.setString(1, id);
			rs=pstmt.executeQuery();
			rs.next();
			System.out.println(result);
			result = rs.getString("connection");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			manager.CloseConnection(con, pstmt,rs);
		}
		return result;
	}

}
