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

/* ��������� Ŭ���� */
public class ModifyCount extends JFrame{
	JComboBox combo;
	JTextField txt;
	JSpinner spin;
	JButton btn;
	String[] item = {"�߰�/���� ����","�߰�","����"};
	ProductDAO dao;
	String num, stock;
	Boolean plus = null;
	StockManagement stockManagement;
	
	
	public ModifyCount(StockManagement stockManagement, String num, String stock) {
		setTitle("����������");
		this.stockManagement = stockManagement;
		this.num = num;
		this.stock = stock;
		
		dao = new ProductDAO();
		combo = new JComboBox(item);
		spin = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
		spin.setPreferredSize(new Dimension(100, 30));
	
		txt = new JTextField(10);
		btn = new JButton("���� ����");
		
		
		// �޺� �̺�Ʈ
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
		
		// ��ư �̺�Ʈ
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(plus == null){
					JOptionPane.showMessageDialog(getParent(), "�߰�/���� ���θ� �������ּ���.");
					return;
				}					
				update();
			}
		});
		
		// ������ �̺�Ʈ
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
		int select = JOptionPane.showConfirmDialog(getParent(), "�����Ͻðڽ��ϱ�?", "��������", JOptionPane.YES_NO_OPTION);
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
				JOptionPane.showMessageDialog(getParent(), "�������!! �ٽýõ����ּ���.");
				return;
			}
			JOptionPane.showMessageDialog(getParent(), "����Ϸ�!!");
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
