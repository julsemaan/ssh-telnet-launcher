package info.wiprogram.sshtelnetlauncher;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Hashtable;
import javax.swing.JScrollPane;


public class View extends JFrame{
	private JTree tree;
	private DefaultMutableTreeNode root;
	private Hashtable<DefaultMutableTreeNode,Connection> bindings;
	private Configuration configuration;
	
	
	public View(Configuration configuration, DBReader database) {
		this.configuration = configuration;
		
		getContentPane().setBackground(Color.WHITE);
		
		root = new DefaultMutableTreeNode("root");
		bindings = new Hashtable<DefaultMutableTreeNode,Connection>();
		this.populateTreeWithVector(database.crawlDatabase(), this.root);
		
		
		
		JPanel panelTree = new JPanel();
		panelTree.setBackground(Color.WHITE);
		panelTree.setMinimumSize(new Dimension(300, 600));
		panelTree.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		tree = new JTree(this.root);
		tree.setAlignmentX(Component.LEFT_ALIGNMENT);
		tree.addMouseListener(new TreeClickListener());
		panelTree.add(tree);
		JScrollPane scrollPane = new JScrollPane(panelTree);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		this.setSize(300,600);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage("ssh.jpg"));
		this.setTitle("SSH and telnet launcher");
	}
	
	private void populateTreeWithVector(NamedVector vector, DefaultMutableTreeNode parent){
		DefaultMutableTreeNode current = new DefaultMutableTreeNode(vector.getName());
		parent.add(current);
		for(Object o : vector){
			if( o instanceof NamedVector ){
				NamedVector v = (NamedVector)(o);
				this.populateTreeWithVector(v, current);
			}
			else if( o instanceof Connection ){
				Connection c = (Connection)(o);
				DefaultMutableTreeNode dmtn = new DefaultMutableTreeNode(c.getName());
				current.add(dmtn);
				bindings.put(dmtn, c);
			}
		}
	}
	
	
	public static void main(String[] args){
		Configuration config = new Configuration("sshtellaunch.conf");

		DBReader database = new DBReader(config.getDatabasePath());
		View v = new View(config, database);
		v.setVisible(true);
		
		
	}
	
	private class TreeClickListener extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e){
			if(e.getClickCount() == 2){
				TreePath tp = tree.getPathForLocation(e.getX(), e.getY());
				if(tp != null){
					DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)tp.getLastPathComponent();
					Connection c = bindings.get(dmtn);
					//System.out.println(c);
					if(c != null){
						//System.out.println(c.getProtocol());
						if(c.getProtocol().equals("SSH")){
							String username=JOptionPane.showInputDialog("Enter username");
							String command = View.this.configuration.getSSHPath()+" "+username+"@"+c.getIp();
							try {
								new ProcessBuilder("xterm", "-e",  command).start();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
						else if(c.getProtocol().equals("Telnet")){
							String command = View.this.configuration.getTelnetPath()+" "+c.getIp();
							try {
								new ProcessBuilder("xterm", "-e",  command).start();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					}
				}
				else{
					System.out.println("Click event received for nothing");
				}
			}
		}
	}

}
