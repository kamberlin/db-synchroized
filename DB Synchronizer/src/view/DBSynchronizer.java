package view;

public class DBSynchronizer extends Thread{
	public boolean running=true;
	public long sleep_sec=1000;
	@Override
	public void run() {
		super.run();
		try {
		while(running) {
			Thread.sleep(sleep_sec);
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public boolean isRunning() {
		return running;
	}
	public void setRunning(boolean running) {
		this.running = running;
	}
	public long getSleep_sec() {
		return sleep_sec;
	}
	public void setSleep_sec(long sleep_sec) {
		this.sleep_sec = sleep_sec;
	}


}
