package time;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
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
				try{
					TimeUnit.SECONDS.sleep(1);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
				try{
					Registry reg = LocateRegistry.getRegistry("127.0.0.1", 
							Variables.getRmiPort());
					TimeOperInt timeOper = (TimeOperInt) reg.lookup("TimeOper");
					timeOper.getNodesTime();
				}catch(RemoteException e){
					e.printStackTrace();
				}catch(NotBoundException e){
					e.printStackTrace();
				}
			}
		}
	}
}
