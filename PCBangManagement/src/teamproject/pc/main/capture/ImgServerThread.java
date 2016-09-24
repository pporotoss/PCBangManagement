package teamproject.pc.main.capture;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

public class ImgServerThread extends Thread{
	ImgServer imgServer;
	InputStream input;
	OutputStream output;
	TestFrame test;
	
	public ImgServerThread(ImgServer imgServer, Socket client, TestFrame test) {
		
		this.imgServer = imgServer;
		this.test = test;
		try {
			input = client.getInputStream();
			output = new FileOutputStream("./capture.jpg");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}// constructor
	
	public void listen(){
		try {
			byte[] sizeAr = new byte[1024];
			input.read(sizeAr);
			int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();

			byte[] imageAr = new byte[size];
			input.read(imageAr);

			BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));
			
			ImageIO.write(image, "jpg", new File("./capture.jpg"));
			if(imgServer.frameOn){
				test.can.render();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{			
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if(input != null){
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public void writeData(byte[] b){
		try {
			output.write(b);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void writeData(int b){
		try {
			output.write(b);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		listen();
	}
}
