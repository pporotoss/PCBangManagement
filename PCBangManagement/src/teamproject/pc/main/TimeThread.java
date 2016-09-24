package teamproject.pc.main;

public class TimeThread extends Thread{
	int countHour = 0;
	int countMinute=0; 
	int countSecond=0;
	MainWindow main;
	int num;
	String user_id;
	boolean onTime = true;
	
	public TimeThread(MainWindow main, int num, String user_id) {
		this.main = main;
		this.num = num;
		this.user_id = user_id;
	}
	
	
	public int getNum() {
		return num;
	}

	public String getUser_id() {
		return user_id;
	}


	@Override
	public void run() {
		while(onTime){
			
			try {
				Thread.sleep(1000);
				if(!onTime){
					return;
				}
				if(countSecond%10 == 0){
					main.setUsePrice(num, user_id);
				}
				setTime();
				main.setUseTime(num, countHour, countMinute, countSecond);
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
		if(countMinute == 60){
			countMinute = 0;
			countHour++;
		}
	}
	public void setOnTime(boolean onTime){
		this.onTime = onTime;
	}
	
}
