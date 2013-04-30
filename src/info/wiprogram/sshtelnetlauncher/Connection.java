package info.wiprogram.sshtelnetlauncher;

public class Connection {
	private String name;
	private String ip;
	private String protocol;
	
	public Connection(String name, String ip, String protocol){
		this.name = name;
		this.ip = ip;
		this.protocol = protocol;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getIp(){
		return this.ip;
	}
	
	public String getProtocol(){
		return this.protocol;
	}

}
