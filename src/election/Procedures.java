package election;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class Procedures implements ProceduresInt{
	private static Logger log = Logger.getLogger(Procedures.class);
	public Nodes publishLeader(){
		BasicConfigurator.configure();
		return Variables.getCurLeader();
	}
	public void setNextNode(Nodes nxtNode){
		Variables.setNextNode(nxtNode);
	}
}
