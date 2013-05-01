package info.wiprogram.sshtelnetlauncher;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class Configuration {
	private PropertiesConfiguration configuration;
	
	public Configuration(String filePath){
		this.configuration = new PropertiesConfiguration();
		try {
			this.configuration.load(filePath);
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getConfigurationProperty(String key){
		return (String)this.configuration.getProperty(key);
	}
	
	public void setDatabasePath(String path){
		this.configuration.setProperty("database_path", path);
	}
	
	public String getDatabasePath(){
		return this.getConfigurationProperty("database_path");
	}
	

	public void setSSHPath(String path){
		this.configuration.setProperty("ssh_path", path);
	}
	
	public String getSSHPath(){
		return this.getConfigurationProperty("ssh_path");
	}
	
	public void setTelnetPath(String path){
		this.configuration.setProperty("telnet_path", path);
	}
	
	public String getTelnetPath(){
		return this.getConfigurationProperty("telnet_path");
	}
	
	
}
