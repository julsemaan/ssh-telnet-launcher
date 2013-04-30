package info.wiprogram.sshtelnetlauncher;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.JSplitPane;
import javax.swing.BoxLayout;

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
	public View() {
		getContentPane().setBackground(Color.WHITE);
		
		root = new DefaultMutableTreeNode("root");
		bindings = new Hashtable<DefaultMutableTreeNode,Connection>();
		DBReader dbr = new DBReader("DATABASE.XML");
		this.populateTreeWithVector(dbr.crawlDatabase(), this.root);
		
		
		
		JPanel panelTree = new JPanel();
		//getContentPane().add(panelTree, BorderLayout.NORTH);
		panelTree.setBackground(Color.WHITE);
		panelTree.setMinimumSize(new Dimension(300, 600));
		panelTree.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		tree = new JTree(this.root);
		tree.setAlignmentX(Component.LEFT_ALIGNMENT);
		tree.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if(e.getClickCount() == 2){
					TreePath tp = tree.getPathForLocation(e.getX(), e.getY());
					if(tp != null){
						DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)tp.getLastPathComponent();
						Connection c = bindings.get(dmtn);
						System.out.println(c);
						if(c != null){
							System.out.println(c.getProtocol());
							if(c.getProtocol().equals("SSH")){
								String username=JOptionPane.showInputDialog("Enter username");
								String command = "/usr/bin/ssh "+username+"@"+c.getIp();
								try {
									new ProcessBuilder("xterm", "-e",  command).start();
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
							else if(c.getProtocol().equals("Telnet")){
								String command = "/usr/bin/telnet "+c.getIp();
								try {
									new ProcessBuilder("xterm", "-e",  command).start();
								} catch (IOException e1) {
									// TODO Auto-generated catch block
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
		});
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
		
		View v = new View();
		v.setVisible(true);

	}

}
