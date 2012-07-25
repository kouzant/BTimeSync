package election;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Utils {
	private int genRanNum(){
		Random rand = new Random();
		int randInt = rand.nextInt();
		
		return randInt;
	}
	private String getSID(){
		InetAddress ip;
		String ipAddr = "";
		String sid = "";
		try{
			ip = InetAddress.getLocalHost();
			ipAddr = ip.getHostAddress();
		}catch(UnknownHostException e){
			e.printStackTrace();
		}
		sid = ipAddr.concat(String.valueOf(genRanNum()));
		
		return sid;
	}
	private static String byteArrayToHexString(byte[] byteArray){
		String result = "";
		for (int i=0; i < byteArray.length; i++) {
			result +=
					Integer.toString( ( byteArray[i] & 0xff ) + 0x100, 16)
					.substring( 1 );
		}
		return result;
	}
	private String sha1(String id){
		String uid = "";
		try{
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.reset();
			uid = byteArrayToHexString(md.digest(id.getBytes("UTF-8")));
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace();
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		
		return uid;
	}
	public String getUID(){
		String sid = getSID();
		String uid = sha1(sid);
		
		return uid;
	}
}
