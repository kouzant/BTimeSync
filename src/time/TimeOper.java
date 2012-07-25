package time;

import java.util.Calendar;

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
	public Calendar appendError(int amount){
		Calendar now = getTime();
		now.add(Calendar.SECOND, amount);
		
		return now;
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
