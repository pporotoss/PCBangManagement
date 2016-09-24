package teamproject.pc.product;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import teamproject.pc.product.dao.ProductDAO;
import teamproject.pc.product.dto.ProductDTO;



public class InsertProduct extends JFrame{
	
	ProductDAO dao;	
	StockManagement sm;
	JPanel p_west, p_center, p_south;
	JLabel[] label = new JLabel[4];
	String[] name_la = {"   상품번호 :  ","   상품명 :  ","   상품가격 :  ","   수  량 :  "};
	JTextField[] txt = new JTextField[4];
	JButton btn;
	
	public InsertProduct(StockManagement sm) {
		dao = new ProductDAO();		
		this.sm = sm;
		setTitle("상품등록");		
		p_west = new JPanel();
		p_center = new JPanel();
		p_south = new JPanel();
		
		// 패널 레이아웃
		p_west.setLayout(new GridLayout(0, 1));
		p_center.setLayout(new GridLayout(0, 1));
		
		for(int i = 0; i < label.length; i++){
			label[i] = new JLabel(name_la[i]);
			label[i].setFont(new Font("dotum", Font.BOLD, 20));
			p_west.add(label[i]);
			txt[i] = new JTextField(15);
			txt[i].setFont(new Font("dotum", Font.BOLD, 20));
			p_center.add(txt[i]);
		}
		
		btn = new JButton("등 록");		
		p_south.add(btn);
		
		// 버튼 이벤트
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				insert();
			}
		});
		
		// 윈도우이벤트
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				sm.setInserProduct(false);
			}
		});
		
		add(p_west, BorderLayout.WEST);
		add(p_center);
		add(p_south, BorderLayout.SOUTH);
		
		setResizable(false);
		Rectangle size = sm.getBounds();
		int xx = (int)(size.getX()+size.getWidth());
		int yy = (int)size.getY();
				
		setBounds(xx, yy, 300, 300);		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);		
	}// 생성자
	
	public void insert(){
		String num = txt[0].getText().trim();		
		String name = txt[1].getText().trim();		
		int price = 0;
		int stock = 0;
		
		if(num.length() == 0){
			JOptionPane.showMessageDialog(getParent(), "상품번호를 입력해주세요.");
			txt[0].requestFocus();
			return;
		}else if(name.length() == 0){
			JOptionPane.showMessageDialog(getParent(), "상품명을 입력해주세요.");
			txt[1].requestFocus();
			return;
		}else if(txt[2].getText().trim().length() == 0){
			JOptionPane.showMessageDialog(getParent(), "가격을 입력해주세요.");
			txt[2].requestFocus();
			return;			
		}else if(txt[3].getText().trim().length() == 0){
			JOptionPane.showMessageDialog(getParent(), "가격을 입력해주세요.");
			txt[3].requestFocus();
			return;
		}
		try {
			price = Integer.parseInt(txt[2].getText().trim());;			
		} catch (NumberFormatException e) {			 
			JOptionPane.showMessageDialog(getParent(), "가격은 숫자만 입력해주세요.");
			return;
		}
		
		try {
			stock = Integer.parseInt(txt[3].getText().trim());;
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(getParent(), "수량은 숫자만 입력해주세요.");
			return;
		}
		
		ProductDTO dto = new ProductDTO();		
		dto.setNum(num);
		dto.setName(name);
		dto.setPrice(price);
		dto.setStock(stock);
		
		int result = dao.insert(dto);
		if(result != 0){
			JOptionPane.showMessageDialog(getParent(), "등록 성공!!");
		}else{
			JOptionPane.showMessageDialog(getParent(), "등록 실패!!");
			return;
		}
		for(int i = 0; i < txt.length; i++){
			txt[i].setText("");
		}
		sm.model.update();
		sm.table.updateUI();
	}
	
	
}
