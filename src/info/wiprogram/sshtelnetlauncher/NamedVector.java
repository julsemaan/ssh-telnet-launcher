package info.wiprogram.sshtelnetlauncher;

import java.util.Vector;

public class NamedVector extends Vector {
	private String name;
	
	public NamedVector(String name){
		super();
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
}
