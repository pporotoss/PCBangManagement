package teamproject.pc.main.stock.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import teamproject.pc.client.db.ConnectionManager;
import teamproject.pc.main.stock.dto.PayDTO;

public class PayDAO {
	Connection con;
	PreparedStatement pstmt;
	ResultSet rs;
	
	ConnectionManager manager=ConnectionManager.getInstane();
	
	ArrayList<PayDTO> payList=new ArrayList<PayDTO>();
	ArrayList<PayDTO> recodeList=new ArrayList<PayDTO>();
	
	public List selectItem(){
		try {
			con=manager.getConnection();
			String sql="select * from pc_product";
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			
			while(rs.next()){
				PayDTO dto=new PayDTO();
				String num=rs.getString("num");
				String name=rs.getString("name");
				int price=rs.getInt("price");
				int stock=rs.getInt("stock");
				
				dto.setNum(num);
				dto.setName(name);
				dto.setPrice(price);
				dto.setStock(stock);
				
				payList.add(dto);
			}
			return payList;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			manager.CloseConnection(con, pstmt, rs);
		}
		return null;
	}
	
	public List getRecode(String numIndex){
		try {
			con=manager.getConnection();
			String sql="select * from pc_product where num=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, numIndex);
			rs=pstmt.executeQuery();
			rs.next();
			PayDTO dto=new PayDTO();
			
			String num=rs.getString("num");
			String name=rs.getString("name");
			int price=rs.getInt("price");
			int stock=rs.getInt("stock");
			
			dto.setNum(num);
			dto.setName(name);
			dto.setPrice(price);
			dto.setStock(stock);
			
			recodeList.add(dto);
									
			return recodeList;
			
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			manager.CloseConnection(con, pstmt, rs);
		}
		return recodeList;
	}
	
	public int update(int stock,String num){	// 재고 수량 변경
		try {
			con=manager.getConnection();
			String sql="update pc_product set stock=? where num=?";
			pstmt=con.prepareStatement(sql);
			
			pstmt.setInt(1, stock);
			pstmt.setString(2, num);
			
			int result=pstmt.executeUpdate();
			
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			manager.CloseConnection(con, pstmt);
		}
		return 0;
	}
	
	public int update(String id, int price){	// 멤버 이용요금 변경
		try {
			con=manager.getConnection();
			String sql="update pc_member set price =? where pc_member_id = ?";
			pstmt=con.prepareStatement(sql);
			
			pstmt.setString(2, id);
			pstmt.setInt(1, price);
			
			int result=pstmt.executeUpdate();
			
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			manager.CloseConnection(con, pstmt);
		}
		return 0;
	}
	
	public int getPrice(String id){
		int price = 0;		
		con = manager.getConnection();
		String sql = "select price from pc_member where pc_member_id = ?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			rs.next();
			price = rs.getInt("price");
		} catch (SQLException e) {			
			e.printStackTrace();
		}finally{
			manager.CloseConnection(con, pstmt, rs);
		}		
		return price;
	}
}
