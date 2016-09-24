package teamproject.pc.product.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import teamproject.pc.product.db.ConnectionManager;
import teamproject.pc.product.dto.ProductDTO;

public class ProductDAO {
	ConnectionManager manager = ConnectionManager.getInstance();
		
	public List selectAll(){	// 전부 다 가져오기.
		ArrayList<ProductDTO> list = new ArrayList<ProductDTO>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
				
		conn = manager.getConnection();
		String sql = "select * from pc_product";
		try {			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				ProductDTO dto = new ProductDTO();
				dto.setNum(rs.getString("num"));
				dto.setName(rs.getString("name"));
				dto.setPrice(rs.getInt("price"));
				dto.setStock(rs.getInt("stock"));
				
				list.add(dto);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			manager.closeConnection(conn, pstmt, rs);
		}		
		return list;
	}
	
	public List select(String field, String col){	// 하나만 검색.
		ArrayList<ProductDTO> list = new ArrayList<ProductDTO>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		conn = manager.getConnection();
		String sql = "select * from pc_product where "+field+"?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, col);			
			
			rs = pstmt.executeQuery();
			while(rs.next()){
				ProductDTO dto = new ProductDTO();
				dto.setNum(rs.getString("num"));
				dto.setName(rs.getString("name"));
				dto.setPrice(rs.getInt("price"));
				dto.setStock(rs.getInt("stock"));
				
				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			manager.closeConnection(conn, pstmt, rs);
		}		
		return list;
	}
	
	public int delete(String num){
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;
				
		conn = manager.getConnection();
		String sql = "delete pc_product where num = ?";
		try {
			pstmt = conn.prepareStatement(sql);			
			pstmt.setString(1, num);
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			manager.closeConnection(conn, pstmt);
		}
		
		return result;
	}
	
	
	public int insert(ProductDTO dto){	// 상품 삽입
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;
		
		conn = manager.getConnection();
		
		String sql = "insert into pc_product(num, name, price, stock)";
		sql += " values(?, ?, ?, ?)";
				
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getNum());
			pstmt.setString(2, dto.getName());
			pstmt.setInt(3, dto.getPrice());
			pstmt.setInt(4, dto.getStock());
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			manager.closeConnection(conn, pstmt);
		}
		
		return result;
	}
	
	public int update(int stock, String num){	// DB재고수량수정
		int result = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		conn = manager.getConnection();
		String sql = "update pc_product set stock = ? where num = ?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, stock);
			pstmt.setString(2, num);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			manager.closeConnection(conn, pstmt);
		}		
		
		return result;
	}
	
	public int updateNum(ProductDTO dto){
		int result = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		conn = manager.getConnection();		
		String sql = "update pc_product set num = ? where name = ? and price = ? and stock = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getNum());
			pstmt.setString(2, dto.getName());
			pstmt.setInt(3, dto.getPrice());
			pstmt.setInt(4, dto.getStock());
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			manager.closeConnection(conn, pstmt);
		}		
		return result;
	}
	
	public int updatePrice(int price, String num){
		int result = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		conn = manager.getConnection();		
		String sql = "update pc_product set price = ? where num = ?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, price);
			pstmt.setString(2, num);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			manager.closeConnection(conn, pstmt);
		}		
		return result;
	}
	
	public int updateName(String name, String num){
		int result = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		conn = manager.getConnection();		
		String sql = "update pc_product set name = ? where num = ?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setString(2, num);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			manager.closeConnection(conn, pstmt);
		}		
		return result;
	}
	
}//class
