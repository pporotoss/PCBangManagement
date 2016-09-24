package teamproject.pc.client.client;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatTingForm extends JFrame {
	JTextArea area;
	JScrollPane scroll;
	JTextField txt;

	public ChatTingForm() {
		area = new JTextArea();
		scroll = new JScrollPane(area);
		txt = new JTextField(20);
		txt.setBackground(Color.green);

		setTitle("Ã¤ÆÃ");
		add(scroll);
		add(txt, "South");
		setSize(300, 400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);

	}
}
