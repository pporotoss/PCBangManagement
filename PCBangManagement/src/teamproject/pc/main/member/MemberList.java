package teamproject.pc.main.member;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import teamproject.pc.main.MainWindow;

public class MemberList extends JFrame implements ActionListener{
	JTable table;
	JPanel p;
	JScrollPane scroll;
	MemberModel model;
	JButton bt;
	MainWindow main;
	
	public MemberList(MainWindow main){
		this.main = main;
		p=new JPanel();
		bt=new JButton("����");
		p.add(bt);
		bt.addActionListener(this);
		
		table=new JTable(model=new MemberModel());
		model.updata();
		table.updateUI();
		scroll=new JScrollPane(table);
					
		setTitle("ȸ������Ʈ");
		
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
		add(scroll);
		add(p,"South");
		setSize(700,700);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		checkDelete();
	}
	
	
	public void close(){
		main.setOnMember(false);
		this.dispose();
	}
	
	
	public void checkDelete(){
		String name=(String)model.getValueAt(table.getSelectedRow(), 1);
		int reply=JOptionPane.showConfirmDialog(getParent(), name+"ȸ���� �����Ͻðڽ��ϱ�");
		if(reply==JOptionPane.YES_OPTION){
			String id=(String)model.getValueAt(table.getSelectedRow(), 0);
			MemberDAO dao=new MemberDAO();
			int result=dao.delete(id);
			if(result==0){
				JOptionPane.showMessageDialog(getParent(), "�����ʵǾ����ϴ�");
				return;
			}else{
				JOptionPane.showMessageDialog(getParent(), "�����Ǿ����ϴ�");
				table.updateUI();
				model.updata();
			}
			
		}else if(reply==JOptionPane.NO_OPTION){
			JOptionPane.showMessageDialog(getParent(), "����ϼ̽��ϴ�");
			return;
		}
		
	}
	
	
	
	
	
}
