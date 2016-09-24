package teamproject.pc.main.stock.pay;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import teamproject.pc.main.MainWindow;
import teamproject.pc.main.stock.dao.PayDAO;
import teamproject.pc.main.stock.dto.PayDTO;

public class StockPayForm extends JFrame {
	JPanel p_north, p_south;
	JComboBox stockBox;
	JTextField t_price;
	JSpinner s_count;
	JButton bt, bt_add, bt_delete;

	DefaultTableModel model;
	JTable table_add;
	JScrollPane table_scroll;
	PayDAO payDAO;

	String row[][];
	String col[] = { "상품번호", "상품명", "상품가격", "상품수" };

	String numIndex;
	ArrayList<String[]> list = new ArrayList<String[]>();
	String selectedNum;
	boolean flag = false;
	MainWindow win;
	int insertPrice;
	String id;
	String ip;
	int selNum;
	
	public StockPayForm(MainWindow win, String id, String ip, int selNum) {
		this.id = id;
		this.win = win;
		this.ip = ip;
		this.selNum = selNum;
		p_north = new JPanel();
		p_south = new JPanel();

		stockBox = new JComboBox<String>();
		t_price = new JTextField(10);
		s_count = new JSpinner();
		s_count.setPreferredSize(new Dimension(50, 20));
		s_count.setValue(1);

		bt = new JButton("계산");
		bt_add = new JButton("추가");
		bt_delete = new JButton("삭제");

		stockBox.addItem("물품명,물품번호");
		getItem();

		p_north.add(stockBox);
		p_north.add(t_price);
		p_north.add(s_count);
		p_north.add(bt_add);
		p_north.add(bt_delete);
		p_south.add(bt);

		add(p_north, "North");
		add(p_south, "South");

		// 테이블생성
		table_add = new JTable(model = new DefaultTableModel(row, col));
		table_scroll = new JScrollPane(table_add);
		add(table_scroll);

		stockBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (stockBox.getSelectedIndex() == 0) {
					JOptionPane.showMessageDialog(getParent(), "물품을선택하세요");
					return;
				} else {
					getContent();
				}
			}
		});

		// 추가버튼
		bt_add.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addRecode();
			}
		});
		// 선택했을떄
		table_add.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table_add.getSelectedRow();
				selectedNum = (String) table_add.getValueAt(row, 0);
			}
		});
		// 삭제버튼
		bt_delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				delete();
			}
		});

		// 계산버튼
		bt.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				calculationContent();
			}
		});
		// 윈도우이벤트
		this.addWindowListener(new WindowAdapter() {
			
			public void windowClosing(WindowEvent e) {
				close();
			}
		});

		setTitle(id + " 물품구매창");
		setSize(500, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setVisible(true);

	}
	
	// 콤보박스 아이템
	public void getItem() {
		payDAO = new PayDAO();
		ArrayList<PayDTO> payList = (ArrayList) payDAO.selectItem();
		for (int i = 0; i < payList.size(); i++) {
			String num = payList.get(i).getNum();
			String name = payList.get(i).getName();
			stockBox.addItem(name + ",물품번호:" + num);
		}

	}

	// db에서 상품번호 이름가져오기
	public void getContent() {
		String str = (String) stockBox.getSelectedItem();
		numIndex = str.substring(str.indexOf(":") + 1);
		payDAO = new PayDAO();
		// 수정
		ArrayList<PayDTO> recodeList = (ArrayList) payDAO.getRecode(numIndex);

		String num = recodeList.get(0).getNum();
		String name = recodeList.get(0).getName();
		int price = recodeList.get(0).getPrice();
		int stock = recodeList.get(0).getStock();
		// System.out.println(num+name+price+stock);

		t_price.setText(price + "원");

	}

	// 추가하기
	public void addRecode() {
		ArrayList<PayDTO> recodeList = (ArrayList) payDAO.getRecode(numIndex);

		String num = recodeList.get(0).getNum();
		String name = recodeList.get(0).getName();
		int price = recodeList.get(0).getPrice();
		int stock = (int) s_count.getValue();

		String priceStr = Integer.toString(price);
		String stockStr = Integer.toString(stock);

		String recode[] = { num, name, priceStr, stockStr };
		model.addRow(recode);

		list.add(recode);
	}

	// 삭제하기
	public void delete() {
		for (int i = 0; i < list.size(); i++) {
			if (table_add.getSelectedRow() < 0) {
				JOptionPane.showMessageDialog(getParent(), "상품을 선택해주세요");
				return;
			}
			// list
			String[] str = list.get(i);
			if (selectedNum == str[0]) {

				model.removeRow(table_add.getSelectedRow());
				list.remove(str);

				return;
			}
		}

	}

	// 계산하기
	public void calculationContent() {
		StringBuffer name = new StringBuffer();
		int totalPrice = 0;
		for (int i = 0; i < list.size(); i++) {
			String[] str = list.get(i);
			name.append(str[1] + ":" + str[2] + "\n");
			totalPrice += Integer.parseInt(str[2]);
		}
		name.append("총금액:" + totalPrice + "입니다");
		int reply = JOptionPane.showConfirmDialog(getParent(), name,"계산",JOptionPane.OK_CANCEL_OPTION);
		if (reply == JOptionPane.YES_OPTION) {			
			calculation(totalPrice);
			JOptionPane.showMessageDialog(getParent(), "계산성공되었습니다");			
			close();
		} else if (reply == JOptionPane.NO_OPTION) {
			return;
		}
	}
	
	// 기존 재고 수정.
	public void calculation(int totalPrice) {
		for (int i = 0; i < list.size(); i++) {
			String[] str = list.get(i);
			String num = str[0];

			// 기존의 재고 가져오기
			ArrayList<PayDTO> recodeList = (ArrayList) payDAO.getRecode(num);
			int defaultStock = recodeList.get(i).getStock();

			String stockStr = str[3];
			int stock = Integer.parseInt(stockStr);

			if (defaultStock - stock < 0) {
				JOptionPane.showMessageDialog(getParent(), num + ":재고없음");
				return;
			}
			payDAO.update(defaultStock - stock, num);	// 재고 업데이트			
		}
		insertPrice(id, totalPrice);
	}

	
	public void insertPrice(String id, int totalPrice){
		payDAO.update(id, totalPrice);// 가격 갱신.
		win.getMainServer().getManager().sendMsg("@@update@@"+ip);
	}
	
	public void close(){
		win.setOnPay(false);
		win.setUsePrice(selNum, id);
		this.dispose();
	}

}
