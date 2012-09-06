package time;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import network.NetOper;

import election.Variables;

public class TimeSync implements Runnable {
	Logger log = Logger.getLogger(TimeSync.class);
	@Override
	public void run() {
		BasicConfigurator.configure();
		while(true){
			try{
				TimeUnit.SECONDS.sleep(Variables.TIME_POLL_INTERVAL);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			log.debug("Outside if statement");
			if(Variables.isLeader() == true){
				log.debug("Inside if statement");
				NetOper netOper = new NetOper();
				netOper.getRtt();
			}
		}
	}
}
