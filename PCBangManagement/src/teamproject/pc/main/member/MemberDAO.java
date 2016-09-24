package teamproject.pc.main.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import teamproject.pc.client.db.ConnectionManager;

public class MemberDAO {
	
	ConnectionManager manager=ConnectionManager.getInstane();
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
		
	public List SelectAll(){
		ArrayList<MemberDTO> list=new ArrayList<MemberDTO>();
		con=manager.getConnection();
		String sql="select * from pc_member";
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			while(rs.next()){
				MemberDTO dto=new MemberDTO();
				String id=rs.getString("pc_member_id");
				String pwd=rs.getString("pwd");
				String name=rs.getString("name");
				String phone=rs.getString("phone");
				int price=rs.getInt("price");
				String start_time=rs.getString("start_time");
				String connection=rs.getString("connection");
				
				dto.setId(id);
				dto.setPwd(pwd);
				dto.setName(name);
				dto.setPhone(phone);
				dto.setPrice(price);
				dto.setStartTime(start_time);
				dto.setConnection(connection);
				
				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			manager.CloseConnection(con, pstmt,rs);
		}
		
		return list;
		
	}
	public int delete(String id){
		int result=0;
		try {
			con=manager.getConnection();
			String sql="delete from pc_member where pc_member_id=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, id);
			result=pstmt.executeUpdate();
			
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	

}
