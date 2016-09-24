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
	Image buffer_img; // 메모리상에 미리 준비될 도화지
	
	public CaptureCanvas() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		this.setPreferredSize(new Dimension(screen.width/2, screen.height/2));		
		
		//this.setPreferredSize(new Dimension(900, 900));		
	}
	
	// 메모리상에 버퍼를 하나 생성하여 원하는 그래픽 처리를 한다.
	public void render(){
		try {
			img = ImageIO.read(new File("./capture.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}		
		buffer_img = this.createImage(this.getWidth(), this.getHeight());	
		// 캔버스 크기만한 가상의 투명한 도화지 생성.
		
		Graphics g = buffer_img.getGraphics();	// 가상의 도화지에서 사용할 붓을 얻어온다.
		
		g.drawImage(img, 0	, 0, null);	// 가상의 도화지 buffer_img에 img를 그린다.		
		 		
		repaint();	// 캔버스의 paint 메서드 호출.
	}
		
	// 메모리상에 이미 그려진 buffer_img를 실제로 컴포넌트에 그림.
	public void paint(Graphics g) {
		//g.drawImage(img, 0	, 0, this);	// 가상의 도화지 buffer_img에 img를 그린다.
		g.drawImage(buffer_img, 0, 0, null);	// 메모리상에 그려놓은 buffer_img를 컴포넌트에 그림. 
	}	
	
	public void update(Graphics g) {
		paint(g);	// 지우개 역할 삭제. 그리기만을 한다. => 지우개를 없애서 깜빡임을 없앰.
	}
}
