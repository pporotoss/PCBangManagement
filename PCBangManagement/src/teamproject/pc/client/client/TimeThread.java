package teamproject.pc.client.client;

public class TimeThread extends Thread {
	int countMinute=0; 
	int countSecond=0;
	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(1000);
				setTime();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public void setTime(){
		countSecond++;
		if(countSecond==60){//60√ 
			countSecond=0;
			countMinute++;
		}
		System.out.println((countMinute+"∫–"+countSecond+"√ "));
	}
}
