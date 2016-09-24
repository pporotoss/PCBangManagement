package teamproject.pc.main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JWindow;

public class LoginWindow extends JWindow{
	LoginCanvas can;
	BufferedImage img;
	
	public LoginWindow() {
		try {
			img = ImageIO.read(new File("./res/sleep.jpg"));
		} catch (IOException e) {		
			e.printStackTrace();
		}
		
		can = new LoginCanvas();
				
		add(can);
		
		
		pack();
		setVisible(true);
		
		Thread th = new Thread(){
			public void run() {
				while(true){
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					can.tick();
					can.render();
				}
			}
		};
		th.start();
	}	
}
