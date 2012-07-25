package time;

import java.util.Calendar;

public class Operations {
	public Calendar getTime(){
		Calendar now = Calendar.getInstance();
		
		return now;
	}
	
	public void printTime(Calendar now){
		StringBuilder time = new StringBuilder();
		time.append(now.get(Calendar.HOUR_OF_DAY))
		.append(":").append(now.get(Calendar.MINUTE))
		.append(":").append(now.get(Calendar.SECOND)).append("\n");
		System.out.println(time.toString());
	}
	public Calendar appendError(Calendar now, int amount){
		now.add(Calendar.SECOND, amount);
		
		return now;
	}
	public static void main(String[] args){
		int amount = 0;
		if (args.length > 0)
			amount = Integer.parseInt(args[0]);
		Operations oper = new Operations();
		System.out.print("Current time: ");
		oper.printTime(oper.getTime());
		System.out.print("Wrong time: ");
		oper.printTime(oper.appendError(oper.getTime(), amount));
	}
}
