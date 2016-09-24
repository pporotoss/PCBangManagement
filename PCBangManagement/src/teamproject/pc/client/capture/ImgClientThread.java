package teamproject.pc.client.capture;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

public class ImgClientThread extends Thread{
	ImgClient imgClient;
	InputStream input;
	OutputStream output;
	boolean writing = true;
	 byte[] imageBytes;
	
	public ImgClientThread(ImgClient imgClient) {
		this.imgClient = imgClient;
		
		try {
			output = imgClient.client.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void bufferW(){
		try {
			BufferedImage image = ImageIO.read(new File("./screenshot.jpg"));
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", byteOut);
			
			byte[] size = ByteBuffer.allocate(1024).putInt(byteOut.size()).array();
			output.write(size);
			output.write(byteOut.toByteArray());
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void listen(){
		int data = 0;
		bufferW();		
			
		if(input != null){
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(output != null){
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(!writing){
			return;
		}
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		imgClient.connect();	
	}
		
	public void sendData(byte[] b){
		try {
			output.write(b);
		} catch (IOException e) {
			writing = false;
			if(input != null){
				try {
					input.close();
				} catch (IOException e1) {
					e.printStackTrace();
				}
			}
			if(output != null){
				try {
					output.close();
				} catch (IOException e1) {
					e.printStackTrace();
				}
			}
			
		}
	}
	public void sendData(int b){
		try {
			output.write(b);
		} catch (IOException e) {
			writing = false;
			if(input != null){
				try {
					input.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(output != null){
				try {
					output.close();
				} catch (IOException e1) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void screen(){	// ½º¼¦Âï±â
		Robot robot = null;
		try {
			robot = new Robot();
		} catch (AWTException e1) {
			e1.printStackTrace();
		}
        String format = "jpg";
        String fileName = "screenshot." + format;
         
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle captureRect = new Rectangle(0, 0, screenSize.width/2, screenSize.height/2);
        BufferedImage screenFullImage = robot.createScreenCapture(captureRect);        
        
        try {
			ImageIO.write(screenFullImage, format, new File(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}        
	}
		
	public void run() {
		screen();
		listen();
	}
}
