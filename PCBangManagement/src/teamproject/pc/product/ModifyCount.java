package teamproject.pc.product;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;

import teamproject.pc.product.dao.ProductDAO;

/* 수량변경용 클래스 */
public class ModifyCount extends JFrame{
	JComboBox combo;
	JTextField txt;
	JSpinner spin;
	JButton btn;
	String[] item = {"추가/삭제 선택","추가","삭제"};
	ProductDAO dao;
	String num, stock;
	Boolean plus = null;
	StockManagement stockManagement;
	
	
	public ModifyCount(StockManagement stockManagement, String num, String stock) {
		setTitle("재고수량변경");
		this.stockManagement = stockManagement;
		this.num = num;
		this.stock = stock;
		
		dao = new ProductDAO();
		combo = new JComboBox(item);
		spin = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
		spin.setPreferredSize(new Dimension(100, 30));
	
		txt = new JTextField(10);
		btn = new JButton("수량 변경");
		
		
		// 콤보 이벤트
		combo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox obj = (JComboBox)e.getSource();
				switch(obj.getSelectedIndex()){
					case 0 :
						plus = null;
						break;
					case 1 :
						plus = true;
						break;
					case 2 :
						plus = false;
						break;
				}				
			}
		});
		
		// 버튼 이벤트
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(plus == null){
					JOptionPane.showMessageDialog(getParent(), "추가/삭제 여부를 선택해주세요.");
					return;
				}					
				update();
			}
		});
		
		// 윈도우 이벤트
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
		setLayout(new FlowLayout());
		
		add(combo);
		add(spin);
		add(btn);
		
		setResizable(false);
		setSize(300,120);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setVisible(true);
		
	}// constructor
	
	public void update(){
		int select = JOptionPane.showConfirmDialog(getParent(), "변경하시겠습니까?", "수량변경", JOptionPane.YES_NO_OPTION);
		if(select == JOptionPane.OK_OPTION){
			int modify = (int) spin.getValue();
			int modifyStock = 0;
			
			if(plus){
				modifyStock = Integer.parseInt(stock)+modify;
			}else if(!plus){
				modifyStock = Integer.parseInt(stock)-modify;
			}
			
			int result = dao.update(modifyStock, num);
			if(result == 0){
				JOptionPane.showMessageDialog(getParent(), "변경실패!! 다시시도해주세요.");
				return;
			}
			JOptionPane.showMessageDialog(getParent(), "변경완료!!");
			stockManagement.model.update();
			stockManagement.table.updateUI();
			close();
		}
	}
	
	public void close(){
		stockManagement.setModifyOn(false);
		dispose();		
	}		
	
}// class
