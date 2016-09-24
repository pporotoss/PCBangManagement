package teamproject.pc.client.client;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JLabel;
import javax.swing.JWindow;

public class ClientMain extends JWindow {
	JLabel la_main;	
	public ClientMain(){
		la_main=new JLabel("시작화면 이미지넣기",JLabel.CENTER);
		
		add(la_main);
		Dimension size=Toolkit.getDefaultToolkit().getScreenSize();
		setSize(size);
		setVisible(true);
	}
	public static void main(String[] args) {
		new ClientMain();
	}
}
