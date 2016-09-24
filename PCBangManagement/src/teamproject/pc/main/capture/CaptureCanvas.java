package teamproject.pc.main.capture;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class CaptureCanvas extends Canvas{
	Image img;
	Image buffer_img; // �޸𸮻� �̸� �غ�� ��ȭ��
	
	public CaptureCanvas() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		this.setPreferredSize(new Dimension(screen.width/2, screen.height/2));		
		
		//this.setPreferredSize(new Dimension(900, 900));		
	}
	
	// �޸𸮻� ���۸� �ϳ� �����Ͽ� ���ϴ� �׷��� ó���� �Ѵ�.
	public void render(){
		try {
			img = ImageIO.read(new File("./capture.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}		
		buffer_img = this.createImage(this.getWidth(), this.getHeight());	
		// ĵ���� ũ�⸸�� ������ ������ ��ȭ�� ����.
		
		Graphics g = buffer_img.getGraphics();	// ������ ��ȭ������ ����� ���� ���´�.
		
		g.drawImage(img, 0	, 0, null);	// ������ ��ȭ�� buffer_img�� img�� �׸���.		
		 		
		repaint();	// ĵ������ paint �޼��� ȣ��.
	}
		
	// �޸𸮻� �̹� �׷��� buffer_img�� ������ ������Ʈ�� �׸�.
	public void paint(Graphics g) {
		//g.drawImage(img, 0	, 0, this);	// ������ ��ȭ�� buffer_img�� img�� �׸���.
		g.drawImage(buffer_img, 0, 0, null);	// �޸𸮻� �׷����� buffer_img�� ������Ʈ�� �׸�. 
	}	
	
	public void update(Graphics g) {
		paint(g);	// ���찳 ���� ����. �׸��⸸�� �Ѵ�. => ���찳�� ���ּ� �������� ����.
	}
}
