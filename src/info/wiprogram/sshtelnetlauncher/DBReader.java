package info.wiprogram.sshtelnetlauncher;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;

public class DBReader {
	private String filePath;
	private Document database;
	
	public DBReader(String filePath){
		this.filePath = filePath;
		this.openDocument();
		//this.crawlDatabase();
	}
	
	private NamedVector getHashForContainer(Node container){
		//System.out.println(container.getNodeName());
		if(container.getNodeName().equals("#text")){
			return null;
		}
		String containerName = container.getAttributes().getNamedItem("name").getNodeValue();
		NamedVector connections = new NamedVector(containerName);
		NodeList children = container.getChildNodes();
		
		for(int i=0; i<children.getLength(); i++){
			Node n = children.item(i);
			if (n.getNodeName().equals("container")){
				NamedNodeMap attributes = n.getAttributes();
				String name = attributes.getNamedItem("name").getNodeValue();
				connections.add(this.getHashForContainer(n));
			}
			else if(n.getNodeName().equals("connection")){
				
				NodeList connection_info = n.getChildNodes().item(1).getChildNodes();
				String ip = "", name = "", protocol = "";
				
				for(int j=0;j<connection_info.getLength();j++){
					Node connectionNode = connection_info.item(j);
					if(connection_info.item(j).getNodeName().equals("name")){
						name = connectionNode.getTextContent();
						
					}
					else if(connection_info.item(j).getNodeName().equals("protocol")){
						protocol = connectionNode.getTextContent();
					}
					else if(connection_info.item(j).getNodeName().equals("host")){
						ip = connectionNode.getTextContent();
					}
				}
				
				if(ip != "" && name != "" && protocol != ""){
					connections.add(new Connection(name, ip, protocol));
				}
				
				
			}
		}
			
		return connections;
		
	}
	
	public NamedVector crawlDatabase(){
		NamedVector root = new NamedVector("root");
		NodeList nl = this.database.getElementsByTagName("configuration").item(0).getChildNodes();
		for(int i=0; i<nl.getLength(); i++){
			Node item = nl.item(i);
			//System.out.println(item.getNodeType());
			root.add(this.getHashForContainer(item));
		}
		return root; 
	}
	
	
	private Document openDocument(){
		try{
			// création d'une fabrique de documents
			DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();
			
			// création d'un constructeur de documents
			DocumentBuilder constructeur = fabrique.newDocumentBuilder();
			
			// lecture du contenu d'un fichier XML avec DOM
			File xml = new File(this.filePath);
			Document document = constructeur.parse(xml);
			
			this.database = document;
		
		}catch(ParserConfigurationException pce){
			System.out.println("Erreur de configuration du parseur DOM");
			System.out.println("lors de l'appel à fabrique.newDocumentBuilder();");
		}catch(SAXException se){
			System.out.println("Erreur lors du parsing du document");
			System.out.println("lors de l'appel à construteur.parse(xml)");
		}catch(IOException ioe){
			System.out.println("Erreur d'entrée/sortie");
			System.out.println("lors de l'appel à construteur.parse(xml)");
		}
		return null;
	}
	
	
}
