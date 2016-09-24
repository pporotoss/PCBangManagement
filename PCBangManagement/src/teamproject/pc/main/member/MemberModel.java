package teamproject.pc.main.member;

import java.sql.Connection;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class MemberModel extends AbstractTableModel {
	String colName[] = { "아이디", "이름", "연락처", "접속확인" };
	String[][] data;
	MemberDAO dao = new MemberDAO();
	ArrayList list;
	public MemberModel() {
		updata();
	}
	public void delete(){
		
	}
	public void updata() {
		list = (ArrayList) dao.SelectAll();
		data = new String[list.size()][colName.length];

		for (int i = 0; i < list.size(); i++) {

			MemberDTO dto = (MemberDTO) list.get(i);
			String id = dto.getId();
			String name = dto.getName();
			String phone = dto.getPhone();
			String connetion = dto.getConnection();
			data[i][0] = id;
			data[i][1] = name;
			data[i][2] = phone;
			data[i][3] = connetion;
		}

	}
	
	@Override
	public int getColumnCount() {
		return colName.length;
	}

	@Override
	public int getRowCount() {
		return data.length;
	}

	@Override
	public String getColumnName(int col) {
		return colName[col];
	}

	@Override
	public void setValueAt(Object v, int rowIndex, int columnIndex) {
		
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return data[rowIndex][columnIndex];
	}

}
