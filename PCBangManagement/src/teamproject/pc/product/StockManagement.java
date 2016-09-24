package teamproject.pc.product;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

import teamproject.pc.main.MainWindow;
import teamproject.pc.product.dao.ProductDAO;

public class StockManagement extends JFrame{
	JPanel p_east, p_search;
	JTextField t_search;
	JTable table;
	JScrollPane scroll;
	JButton bt_insert, bt_delete, bt_search, bt_all;
	JComboBox combo;
	InsertProduct ip;
	boolean insertProductOn = false;	// ��ǰ���â Ȱ��ȭ ����.
	boolean modifyOn = false;	// ������ ����â Ȱ��ȭ ����
	ProductModel model;
	int stockClickCount;
	ModifyCount modify;
	String oldValue;
	int oldRow = -1; // ������ ������ ��. 
	ProductDAO dao;
	String[] comboItem = {"��  ��","��ǰ��ȣ","��ǰ��"};
	MainWindow win;
	
	public StockManagement(MainWindow win) {
		this.win = win;
		setTitle("������");
		StockManagement me = this;
		dao = new ProductDAO();
		p_east = new JPanel();
		table = new JTable(model = new ProductModel());
		scroll = new JScrollPane(table);
		bt_insert = new JButton("��ǰ ���");
		bt_delete = new JButton("��ǰ ����");		
		p_search = new JPanel();
		t_search = new JTextField(20);
		bt_search = new JButton("�� ��");
		bt_all = new JButton("��ü����");
		combo = new JComboBox(comboItem);
		
				
		// ���̺� ��� ����
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		renderer.setHorizontalAlignment(SwingConstants.CENTER);
		TableColumnModel tcm = table.getColumnModel();
		tcm.getColumn(1).setCellRenderer(renderer);		
		
								
		// ��ư �̺�Ʈ
		bt_insert.addActionListener(new ActionListener() {// ��Ϲ�ư
			public void actionPerformed(ActionEvent e) {
				insertProduct();
			}
		});
		
		bt_delete.addActionListener(new ActionListener() {	// ������ư
			public void actionPerformed(ActionEvent e) {
				productDelete();
			}
		});
		
		bt_search.addActionListener(new ActionListener() {// �˻���ư
			public void actionPerformed(ActionEvent e) {
				int sel = combo.getSelectedIndex();
				String field = "";
				StringBuffer col = null;
				switch(sel){
					case 0 :
						JOptionPane.showMessageDialog(getParent(), "�˻� ������ ������ �ּ���.");
						return;
					case 1 :
						field = "num = ";
						col = new StringBuffer(t_search.getText().trim());
						break;
					case 2 :
						field = "name like ";
						col = new StringBuffer("%"+(t_search.getText().trim())+"%");
						break;
				}// switch
				if(col.length() == 0){
					JOptionPane.showMessageDialog(getParent(), "�˻�� �Է��� �ּ���.");
					return;
				}
				t_search.setText("");
				
				int result = model.select(field, col.toString());
				if(result == 0){
					JOptionPane.showMessageDialog(getParent(), "��ġ�ϴ� ��ǰ�� �����ϴ�.");
					model.update();					
				}				
				table.updateUI();
			}
		});
		
		bt_all.addActionListener(new ActionListener() {	// ��ü����
			public void actionPerformed(ActionEvent e) {
				model.update();
				table.updateUI();
			}
		});
						
		// ���̺� �̺�Ʈ		
		table.addMouseListener(new MouseAdapter() {	// ���̺� ���콺 �̺�Ʈ			
			public void mouseClicked(MouseEvent e) {				
				JTable obj = (JTable)e.getSource();
				String name = obj.getColumnName(obj.getSelectedColumn());
				int row = obj.getSelectedRow();
				int col = obj.getSelectedColumn();
				oldValue = (String) obj.getValueAt(row, col);				
				
				if(name.equals("������")){
					stockClickCount++;
					if(stockClickCount == 1){
						oldRow = row;						
					}else if(stockClickCount == 2 && oldRow == row){
						String num = (String) model.getValueAt(row, 0);
						String stock = (String) model.getValueAt(row, col);
						
						stockClickCount = 0;						
						if(!modifyOn){							
							modify = new ModifyCount(me, num, stock);
							modifyOn = true;
						}
					}else if(oldRow != row){
						stockClickCount = 0;
					}// if stockClickCount
				}else{
					stockClickCount = 0;	// ���� Ŭ���ϸ� �ʱ�ȭ.
				}// if name
				
				
			}// mouseClicked
		});
			
		
		// ������ ������
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int select = JOptionPane.showConfirmDialog(getParent(), "�������� �����Ͻðڽ��ϱ�?","����",JOptionPane.OK_CANCEL_OPTION);
				
				if(select == JOptionPane.OK_OPTION){					
					closeManagement();					
					if(ip != null){
						ip.dispose();
					}
				}				
			}
		});
		
		// ���̱�
		p_search.add(combo);
		p_search.add(t_search);
		p_search.add(bt_search);
		p_search.add(bt_all);
		
		p_east.setPreferredSize(new Dimension(100, 0));
		p_east.add(bt_insert);
		p_east.add(bt_delete);
		
		add(p_search, BorderLayout.NORTH);
		add(scroll);
		add(p_east, BorderLayout.EAST);
		
		setSize(900,600);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	// ����â ����.
	public void insertProduct(){
		if(!insertProductOn){	// Ȱ��ȭ�� ����â�� ������
			ip = new InsertProduct(this);
			insertProductOn = true;
		}else{
			JOptionPane.showMessageDialog(getParent(), "�̹� Ȱ��ȭ �� ��ǰ��� â�� �����մϴ�.");
		}
	}
	
	// ��ǰ ����
	public void productDelete(){
		int row = table.getSelectedRow();		
		if(row == -1){	// ���̺� ���� ��������,
			JOptionPane.showMessageDialog(getParent(), "������ ��ǰ�� �������ּ���.");
			return;
		}
		String num = (String) table.getValueAt(row, 0);
		
		int sel = JOptionPane.showConfirmDialog(getParent(), "�����Ͻ� ��ǰ��ȣ : "+num+"�� ��ǰ�� �����Ͻðڽ��ϱ�?", "����", JOptionPane.OK_CANCEL_OPTION);
		if(sel == JOptionPane.CANCEL_OPTION){			
			return;
		}
		int result = dao.delete(num);
		if(result == 0){
			JOptionPane.showMessageDialog(getParent(), "���� ����!!");
			return;
		}
		JOptionPane.showMessageDialog(getParent(), "���� ����!!");
		model.update();
		table.updateUI();
	}
	
	// ��ǰ���â Ȱ������
	public void setInserProduct(boolean insertProductOn){
		this.insertProductOn = insertProductOn;
	}
	
	// ������ ���� â Ȱ������
	public void setModifyOn(boolean modifyOn){
		this.modifyOn = modifyOn;
	}
	
	// ������ â ����
	public void closeManagement(){
		win.setOnStock(false);
		dispose();
	}	
}
