package info.wiprogram.sshtelnetlauncher;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class Configuration {
	private PropertiesConfiguration configuration;
	private String filePath;
	
	public Configuration(String filePath) throws Exception{
		this.configuration = new PropertiesConfiguration();
		this.filePath = filePath; 
		try {
			this.configuration.load(filePath);
		} catch (ConfigurationException e) {
			throw new Exception("Error while reading configuration. Does the file "+this.filePath+" exist ?");
		}
	}
	
	private void save() throws Exception{
		this.configuration.save(this.filePath);
	}
	
	public String getConfigurationProperty(String key){
		return (String)this.configuration.getProperty(key);
	}
	
	public void setDatabasePath(String path) throws Exception{
		this.configuration.setProperty("database_path", path);
		this.save();
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
