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
	boolean insertProductOn = false;	// 상품등록창 활성화 여부.
	boolean modifyOn = false;	// 재고수량 변경창 활성화 여부
	ProductModel model;
	int stockClickCount;
	ModifyCount modify;
	String oldValue;
	int oldRow = -1; // 이전에 선택한 줄. 
	ProductDAO dao;
	String[] comboItem = {"선  택","상품번호","상품명"};
	MainWindow win;
	
	public StockManagement(MainWindow win) {
		this.win = win;
		setTitle("재고관리");
		StockManagement me = this;
		dao = new ProductDAO();
		p_east = new JPanel();
		table = new JTable(model = new ProductModel());
		scroll = new JScrollPane(table);
		bt_insert = new JButton("상품 등록");
		bt_delete = new JButton("상품 삭제");		
		p_search = new JPanel();
		t_search = new JTextField(20);
		bt_search = new JButton("검 색");
		bt_all = new JButton("전체보기");
		combo = new JComboBox(comboItem);
		
				
		// 테이블 가운데 정렬
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		renderer.setHorizontalAlignment(SwingConstants.CENTER);
		TableColumnModel tcm = table.getColumnModel();
		tcm.getColumn(1).setCellRenderer(renderer);		
		
								
		// 버튼 이벤트
		bt_insert.addActionListener(new ActionListener() {// 등록버튼
			public void actionPerformed(ActionEvent e) {
				insertProduct();
			}
		});
		
		bt_delete.addActionListener(new ActionListener() {	// 삭제버튼
			public void actionPerformed(ActionEvent e) {
				productDelete();
			}
		});
		
		bt_search.addActionListener(new ActionListener() {// 검색버튼
			public void actionPerformed(ActionEvent e) {
				int sel = combo.getSelectedIndex();
				String field = "";
				StringBuffer col = null;
				switch(sel){
					case 0 :
						JOptionPane.showMessageDialog(getParent(), "검색 유형을 선택해 주세요.");
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
					JOptionPane.showMessageDialog(getParent(), "검색어를 입력해 주세요.");
					return;
				}
				t_search.setText("");
				
				int result = model.select(field, col.toString());
				if(result == 0){
					JOptionPane.showMessageDialog(getParent(), "일치하는 상품이 없습니다.");
					model.update();					
				}				
				table.updateUI();
			}
		});
		
		bt_all.addActionListener(new ActionListener() {	// 전체보기
			public void actionPerformed(ActionEvent e) {
				model.update();
				table.updateUI();
			}
		});
						
		// 테이블 이벤트		
		table.addMouseListener(new MouseAdapter() {	// 테이블 마우스 이벤트			
			public void mouseClicked(MouseEvent e) {				
				JTable obj = (JTable)e.getSource();
				String name = obj.getColumnName(obj.getSelectedColumn());
				int row = obj.getSelectedRow();
				int col = obj.getSelectedColumn();
				oldValue = (String) obj.getValueAt(row, col);				
				
				if(name.equals("재고수량")){
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
					stockClickCount = 0;	// 딴거 클릭하면 초기화.
				}// if name
				
				
			}// mouseClicked
		});
			
		
		// 윈도우 리스너
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int select = JOptionPane.showConfirmDialog(getParent(), "재고관리를 종료하시겠습니까?","종료",JOptionPane.OK_CANCEL_OPTION);
				
				if(select == JOptionPane.OK_OPTION){					
					closeManagement();					
					if(ip != null){
						ip.dispose();
					}
				}				
			}
		});
		
		// 붙이기
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
	
	// 삽입창 띄우기.
	public void insertProduct(){
		if(!insertProductOn){	// 활성화된 삽입창이 없으면
			ip = new InsertProduct(this);
			insertProductOn = true;
		}else{
			JOptionPane.showMessageDialog(getParent(), "이미 활성화 된 상품등록 창이 존재합니다.");
		}
	}
	
	// 상품 삭제
	public void productDelete(){
		int row = table.getSelectedRow();		
		if(row == -1){	// 테이블 선택 안했으면,
			JOptionPane.showMessageDialog(getParent(), "삭제할 상품을 선택해주세요.");
			return;
		}
		String num = (String) table.getValueAt(row, 0);
		
		int sel = JOptionPane.showConfirmDialog(getParent(), "선택하신 상품번호 : "+num+"번 상품을 삭제하시겠습니까?", "삭제", JOptionPane.OK_CANCEL_OPTION);
		if(sel == JOptionPane.CANCEL_OPTION){			
			return;
		}
		int result = dao.delete(num);
		if(result == 0){
			JOptionPane.showMessageDialog(getParent(), "삭제 실패!!");
			return;
		}
		JOptionPane.showMessageDialog(getParent(), "삭제 성공!!");
		model.update();
		table.updateUI();
	}
	
	// 상품등록창 활성여부
	public void setInserProduct(boolean insertProductOn){
		this.insertProductOn = insertProductOn;
	}
	
	// 재고수량 변경 창 활성여부
	public void setModifyOn(boolean modifyOn){
		this.modifyOn = modifyOn;
	}
	
	// 재고관리 창 종료
	public void closeManagement(){
		win.setOnStock(false);
		dispose();
	}	
}
