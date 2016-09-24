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
	String col[] = { "��ǰ��ȣ", "��ǰ��", "��ǰ����", "��ǰ��" };

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

		bt = new JButton("���");
		bt_add = new JButton("�߰�");
		bt_delete = new JButton("����");

		stockBox.addItem("��ǰ��,��ǰ��ȣ");
		getItem();

		p_north.add(stockBox);
		p_north.add(t_price);
		p_north.add(s_count);
		p_north.add(bt_add);
		p_north.add(bt_delete);
		p_south.add(bt);

		add(p_north, "North");
		add(p_south, "South");

		// ���̺����
		table_add = new JTable(model = new DefaultTableModel(row, col));
		table_scroll = new JScrollPane(table_add);
		add(table_scroll);

		stockBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (stockBox.getSelectedIndex() == 0) {
					JOptionPane.showMessageDialog(getParent(), "��ǰ�������ϼ���");
					return;
				} else {
					getContent();
				}
			}
		});

		// �߰���ư
		bt_add.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addRecode();
			}
		});
		// ����������
		table_add.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table_add.getSelectedRow();
				selectedNum = (String) table_add.getValueAt(row, 0);
			}
		});
		// ������ư
		bt_delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				delete();
			}
		});

		// ����ư
		bt.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				calculationContent();
			}
		});
		// �������̺�Ʈ
		this.addWindowListener(new WindowAdapter() {
			
			public void windowClosing(WindowEvent e) {
				close();
			}
		});

		setTitle(id + " ��ǰ����â");
		setSize(500, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setVisible(true);

	}
	
	// �޺��ڽ� ������
	public void getItem() {
		payDAO = new PayDAO();
		ArrayList<PayDTO> payList = (ArrayList) payDAO.selectItem();
		for (int i = 0; i < payList.size(); i++) {
			String num = payList.get(i).getNum();
			String name = payList.get(i).getName();
			stockBox.addItem(name + ",��ǰ��ȣ:" + num);
		}

	}

	// db���� ��ǰ��ȣ �̸���������
	public void getContent() {
		String str = (String) stockBox.getSelectedItem();
		numIndex = str.substring(str.indexOf(":") + 1);
		payDAO = new PayDAO();
		// ����
		ArrayList<PayDTO> recodeList = (ArrayList) payDAO.getRecode(numIndex);

		String num = recodeList.get(0).getNum();
		String name = recodeList.get(0).getName();
		int price = recodeList.get(0).getPrice();
		int stock = recodeList.get(0).getStock();
		// System.out.println(num+name+price+stock);

		t_price.setText(price + "��");

	}

	// �߰��ϱ�
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

	// �����ϱ�
	public void delete() {
		for (int i = 0; i < list.size(); i++) {
			if (table_add.getSelectedRow() < 0) {
				JOptionPane.showMessageDialog(getParent(), "��ǰ�� �������ּ���");
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

	// ����ϱ�
	public void calculationContent() {
		StringBuffer name = new StringBuffer();
		int totalPrice = 0;
		for (int i = 0; i < list.size(); i++) {
			String[] str = list.get(i);
			name.append(str[1] + ":" + str[2] + "\n");
			totalPrice += Integer.parseInt(str[2]);
		}
		name.append("�ѱݾ�:" + totalPrice + "�Դϴ�");
		int reply = JOptionPane.showConfirmDialog(getParent(), name,"���",JOptionPane.OK_CANCEL_OPTION);
		if (reply == JOptionPane.YES_OPTION) {			
			calculation(totalPrice);
			JOptionPane.showMessageDialog(getParent(), "��꼺���Ǿ����ϴ�");			
			close();
		} else if (reply == JOptionPane.NO_OPTION) {
			return;
		}
	}
	
	// ���� ��� ����.
	public void calculation(int totalPrice) {
		for (int i = 0; i < list.size(); i++) {
			String[] str = list.get(i);
			String num = str[0];

			// ������ ��� ��������
			ArrayList<PayDTO> recodeList = (ArrayList) payDAO.getRecode(num);
			int defaultStock = recodeList.get(i).getStock();

			String stockStr = str[3];
			int stock = Integer.parseInt(stockStr);

			if (defaultStock - stock < 0) {
				JOptionPane.showMessageDialog(getParent(), num + ":������");
				return;
			}
			payDAO.update(defaultStock - stock, num);	// ��� ������Ʈ			
		}
		insertPrice(id, totalPrice);
	}

	
	public void insertPrice(String id, int totalPrice){
		payDAO.update(id, totalPrice);// ���� ����.
		win.getMainServer().getManager().sendMsg("@@update@@"+ip);
	}
	
	public void close(){
		win.setOnPay(false);
		win.setUsePrice(selNum, id);
		this.dispose();
	}

}
