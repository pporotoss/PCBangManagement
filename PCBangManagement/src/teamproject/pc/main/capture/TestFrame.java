package teamproject.pc.main.capture;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class TestFrame extends JFrame{
	CaptureCanvas can;	
	ImgServer imgServer;
	
	public TestFrame(ImgServer imgServer) {
		this.imgServer = imgServer;
		
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
		
		can = new CaptureCanvas();
		add(can);
		pack();
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setVisible(true);		
	}	
	
	public void close(){
		imgServer.frameOn = false;
		imgServer.closeServer();
		dispose();
	}
	
}
