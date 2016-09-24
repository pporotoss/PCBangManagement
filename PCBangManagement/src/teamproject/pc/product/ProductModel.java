package teamproject.pc.product;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import teamproject.pc.product.dao.ProductDAO;
import teamproject.pc.product.dto.ProductDTO;

public class ProductModel extends AbstractTableModel{
	String[] colName = {"��ǰ��ȣ","��ǰ��","�� ��","������"};
	String[][] data;
	ProductDAO dao;
	
	public ProductModel() {
		dao = new ProductDAO();
		
		update();
	}
	
	/* ���� ���� �޼��� */
	public void update(){	// ���̺���� data �迭�� ����.
		List<ProductDTO> list =  dao.selectAll();
		data = new String[list.size()][colName.length];
		for(int li = 0; li < list.size(); li++){
			ProductDTO dto = list.get(li);
			data[li][0] = dto.getNum();
			data[li][1] = dto.getName();
			data[li][2] = Integer.toString(dto.getPrice());
			data[li][3] = Integer.toString(dto.getStock());;			
		}
	}
	
	public int select(String field, String col){	// Ư������ �����ͼ� data�迭 ����.	
		List<ProductDTO> list =  dao.select(field, col);
		int result = list.size();
		data = new String[list.size()][colName.length];
		for(int li = 0; li < list.size(); li++){
			ProductDTO dto = list.get(li);
			data[li][0] = dto.getNum();
			data[li][1] = dto.getName();
			data[li][2] = Integer.toString(dto.getPrice());
			data[li][3] = Integer.toString(dto.getStock());;			
		}
		return result;
	}	
	
	
	/* ������ �޼��� */
	public boolean isCellEditable(int row, int col) {
		if(col != 3){
			return true;
		}else{
			return false;
		}		
	};
	
	
	public String getColumnName(int col) {
		return colName[col];
	}
	
	public int getColumnCount() {
		return colName.length;
	}

	public int getRowCount() {
		return data.length;
	}

	public Object getValueAt(int row, int col) {
		return data[row][col];
	}
		
	public Class<?> getColumnClass(int col) {
		Class<?> dataType = super.getColumnClass(col);
		if(col == 3 || col == 2){
			dataType = Integer.class;
		}
		
		return dataType;
	}
	
	
	public void setValueAt(Object aValue, int row, int col) {
		if(col == 0){
			ProductDTO dto = new ProductDTO();
			dto.setNum((String)aValue);
			dto.setName(data[row][1]);
			dto.setPrice(Integer.parseInt(data[row][2]));
			dto.setStock(Integer.parseInt(data[row][3]));
			dao.updateNum(dto);
		}else if(col == 1){			
			dao.updateName((String)aValue, data[row][0]);			
		}else if(col == 2){	
			try {
				int price = (int)aValue;	
				dao.updatePrice(price, data[row][0]);
			} catch (NumberFormatException e) { // �Է°��� ���ڰ� �ƴϸ� ����.				
				return;
			}
		}
		update();
	}
	
	public void addTableModelListener(TableModelListener l) {
		super.addTableModelListener(l);
	}
	
	
}
