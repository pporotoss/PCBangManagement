package teamproject.pc.main;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class LoginCanvas extends Canvas{
	Image img;
	Image buffer_img;
	boolean flag = true;
	boolean yFlag = true;
	Dimension screen;
	int xx, yy = 800;
	
	public LoginCanvas() {
		screen = Toolkit.getDefaultToolkit().getScreenSize();
		this.setPreferredSize(screen);	
	}
	// �޸𸮻� ���۸� �ϳ� �����Ͽ� ���ϴ� �׷��� ó���� �Ѵ�.
		
	public void render(){
			try {
				img = ImageIO.read(new File("./res/sleep.jpg"));
			} catch (IOException e) {
				e.printStackTrace();
			}		
			buffer_img = this.createImage(this.getWidth(), this.getHeight());	
			// ĵ���� ũ�⸸�� ������ ������ ��ȭ�� ����.
			
			Graphics g = buffer_img.getGraphics();	// ������ ��ȭ������ ����� ���� ���´�.
			
			g.drawImage(img, xx, 0, null);	// ������ ��ȭ�� buffer_img�� img�� �׸���.		
			g.drawImage(img, yy, 0, null);	// ������ ��ȭ�� buffer_img�� img�� �׸���.
			
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
		
		public void tick(){
			if(flag){
				xx+=5;				
			}else{
				xx-=5;				
			}
			if(yFlag){
				yy+=10;
			}else{
				yy-=10;
			}
			if(xx >= screen.getWidth() || xx < 0){
				flag = !flag;
			}
			if(yy >= screen.getWidth() || yy < 0){
				yFlag = !yFlag;
			}
			
		}
}
