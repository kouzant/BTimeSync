package time;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;

import election.Nodes;
import election.Variables;

public class TimeOper implements TimeOperInt{
	private int amount = 0;
	
	public void setAmount(int amount){
		this.amount = amount;
	}
	public Calendar getTime(){
		Calendar now = Calendar.getInstance();
		
		return now;
	}
	public String printTime(Calendar now){
		StringBuilder time = new StringBuilder();
		time.append(now.get(Calendar.HOUR_OF_DAY))
		.append(":").append(now.get(Calendar.MINUTE))
		.append(":").append(now.get(Calendar.SECOND)).append("\n");
		
		return time.toString();
	}
	public Calendar appendError(){
		Calendar now = getTime();
		now.add(Calendar.SECOND, amount);
		
		return now;
	}
	public long getRunningTime(){
		Calendar time = appendError();
		long timeLong = time.getTimeInMillis();
		return timeLong;
	}
	public void getNodesTime(){
		LinkedList<Nodes> nodesList = Variables.getNodesList();
		Nodes indexNode = null;
		Iterator<Nodes> nodesIter = nodesList.iterator();
		while(nodesIter.hasNext()){
			indexNode = nodesIter.next();
			try{
				Registry reg = LocateRegistry.getRegistry(indexNode.getIpAddr(),
						indexNode.getRmiPort());
				TimeOperInt timeOper = (TimeOperInt) reg.lookup("TimeOper");
				long time = timeOper.getRunningTime();
				long rtt = indexNode.getRtt();
				indexNode.setTime(time + (rtt / 2));
			}catch(RemoteException e){
				e.printStackTrace();
			}catch(NotBoundException e){
				e.printStackTrace();
			}
		}
	}
	public String printWrongTime(){
		Calendar now = getTime();
		now.add(Calendar.SECOND, amount);
		String time = printTime(now);
		
		return time;
	}
	public void fixErrorAmount(int fix){
		setAmount(amount + fix);
	}
}
