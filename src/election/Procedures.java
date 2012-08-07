package election;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class Procedures implements ProceduresInt{
	private static Logger log = Logger.getLogger(Procedures.class);
	public Nodes publishLeader(){
		BasicConfigurator.configure();
		log.debug(Variables.getCurLeader());
		return Variables.getCurLeader();
	}
}
