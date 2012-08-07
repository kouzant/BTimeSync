package time;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigParser {
	private static Properties properties=new Properties();

	public ConfigParser(String filename){
		try{
			System.out.println(filename);
			FileInputStream fis=new FileInputStream(filename);
			properties.load(fis);
		}catch(FileNotFoundException e0){
			System.err.println("Could not load BTimeSync configuration file");
			System.exit(1);
		}catch(IOException e1){
			System.err.println("Could not load BTimeSync configuration file");
			System.exit(1);
		}
	}
	
	public int isLeader(){
		String lead = properties.getProperty("leader", "0");
		return Integer.parseInt(lead);
	}
	public int timeError(){
		String terror = properties.getProperty("terror", "5");
		return Integer.parseInt(terror);
	}
	public int rmiPort(){
		String port = properties.getProperty("rmiport", "2080");
		return Integer.parseInt(port);
	}
	public String bootstrap(){
		return properties.getProperty("bootstrap", " ");
	}
	public int b_port(){
		String b_port = properties.getProperty("b_port", " ");
		return Integer.parseInt(b_port);
	}
}
