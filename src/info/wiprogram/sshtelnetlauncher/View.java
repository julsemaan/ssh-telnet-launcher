package info.wiprogram.sshtelnetlauncher;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.CardLayout;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.commons.configuration.ConfigurationException;



public class View extends JFrame{
	private JTree tree;
	private DefaultMutableTreeNode root;
	private Hashtable<DefaultMutableTreeNode,Connection> bindings;
	private Configuration configuration;
	private DBReader database;
	private JTextField ipField;
	private JComboBox comboProtocols;
	private JMenuItem mntmSelectDatabase;
	private JMenuItem mntmOptions;
	private JPanel panelTree;
	private JScrollPane scrollPane;
	
	public View(Configuration configuration, DBReader database) throws Exception{
		this.configuration = configuration;
		this.database = database;
		
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		
		getContentPane().setBackground(Color.WHITE);
		
		root = new DefaultMutableTreeNode("root");
		bindings = new Hashtable<DefaultMutableTreeNode,Connection>();
		this.populateTreeWithVector(database.crawlDatabase(), this.root);
		
		String protocols[] = {"SSH", "Telnet"};
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JMenuBar menuBar = new JMenuBar();
		getContentPane().add(menuBar, BorderLayout.NORTH);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mntmSelectDatabase = new JMenuItem("Select database");
		this.mntmSelectDatabase.addActionListener(new MenuListener());
		mnFile.add(mntmSelectDatabase);
		
		mntmOptions = new JMenuItem("Options");
		mnFile.add(mntmOptions);
		
		JPanel mainPanel = new JPanel();
		getContentPane().add(mainPanel);
		mainPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel panelManualConnection = new JPanel();
		mainPanel.add(panelManualConnection, BorderLayout.NORTH);
		panelManualConnection.setPreferredSize(new Dimension(400, 45));
		panelManualConnection.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JPanel ipPanel = new JPanel();
		panelManualConnection.add(ipPanel);
		ipPanel.setLayout(new FlowLayout());
		
		JLabel labelIp = new JLabel("IP : ");
		ipPanel.add(labelIp);
		
		ipField = new JTextField();
		ipField.setPreferredSize(new Dimension(4, 25));
		ipPanel.add(ipField);
		ipField.setColumns(10);
		ipField.addActionListener(new ManualConnectionListener());
		
		JPanel panel = new JPanel();
		panelManualConnection.add(panel);
		comboProtocols = new JComboBox(protocols);
		panel.add(comboProtocols);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ManualConnectionListener());
		panel.add(btnConnect);
		
		
		
		panelTree = new JPanel();
		panelTree.setBackground(Color.WHITE);
		//panelTree.setMinimumSize(new Dimension(300, 500));
		panelTree.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		tree = new JTree(this.root);
		tree.setAlignmentX(Component.LEFT_ALIGNMENT);
		tree.addMouseListener(new TreeClickListener());
		panelTree.add(tree);
		scrollPane = new JScrollPane(panelTree);
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		//scrollPane.setPreferredSize(new Dimension(300,450));
		this.setSize(400,600);
		this.setMinimumSize(new Dimension(400,200));
		this.setIconImage(Toolkit.getDefaultToolkit().getImage("icon.jpg"));
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
				DefaultMutableTreeNode dmtn = new DefaultMutableTreeNode(c.getName()+" - "+c.getIp());
				current.add(dmtn);
				
				bindings.put(dmtn, c);
			}
		}
	}
	
	private class TreeClickListener extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e){
			if(e.getClickCount() == 2){
				TreePath tp = tree.getPathForLocation(e.getX(), e.getY());
				if(tp != null){
					DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)tp.getLastPathComponent();
					Connection c = bindings.get(dmtn);
					View.this.openConnection(c);
				}
				else{
					System.out.println("Click event received for nothing");
				}
			}
		}
	}
	
	private class ManualConnectionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			//System.out.println((String)View.this.comboProtocols.getSelectedItem());
			Connection c = new Connection("", View.this.ipField.getText(), (String)View.this.comboProtocols.getSelectedItem());
			View.this.openConnection(c);
		}
	}
	
	private class MenuListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			JMenuItem source = (JMenuItem)e.getSource();
			if( source == View.this.mntmSelectDatabase ){
				String originalDatabasePath = View.this.database.getFilePath();
				JFileChooser fc = new JFileChooser();
				int result = fc.showOpenDialog(View.this);
				if (result == fc.APPROVE_OPTION){
					File f = fc.getSelectedFile();
					try {
						View.this.database = new DBReader(fc.getSelectedFile().getAbsolutePath());
					}
					catch (Exception e1) {
						JOptionPane.showMessageDialog(View.this, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					try {
						DefaultMutableTreeNode newRoot = new DefaultMutableTreeNode("root");
						View.this.populateTreeWithVector(View.this.database.crawlDatabase(), newRoot);
						JTree newTree = new JTree(newRoot);
						//really ugly but doesn't want to repaint without that....
						newTree.setSize(new Dimension(100,100));
						
						View.this.root = newRoot;
						View.this.panelTree.add(newTree);
						View.this.panelTree.remove(View.this.tree);
						View.this.tree = newTree;
						View.this.root = newRoot;
						View.this.tree.addMouseListener(new TreeClickListener());
						View.this.repaint();
							

					} catch (Exception e1) {
						JOptionPane.showMessageDialog(View.this, "Error while reading database.\nMake sure it is a PuttyCM database", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					try{
						View.this.configuration.setDatabasePath(f.getAbsolutePath());
					}
					catch (ConfigurationException e1) {
						JOptionPane.showMessageDialog(View.this, "Error while saving configuration.\nDoes the config file exist ?\nDoes the database you selected exists ?", "Error", JOptionPane.ERROR_MESSAGE);
					}
					
					
				}
			}
		}
	}
	
	private void openConnection(Connection conn){
		//System.out.println(c);
		if(conn != null){
			String title;
			if(conn.getName().isEmpty()){
				title = conn.getIp();
			}
			else{
				title = conn.getName();
			}
			//System.out.println(c.getProtocol());
			if(conn.getProtocol().equals("SSH")){
				String username=JOptionPane.showInputDialog("Enter username");
				if(username != null){
					String command;
					if(username == ""){
						command = this.configuration.getSSHPath()+" -l '' "+conn.getIp();
					}
					else{
						command = this.configuration.getSSHPath()+" "+username+"@"+conn.getIp();
					}
					try {
						new ProcessBuilder("xterm", "-T", title, "-n", title,  "-e",  command).start();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
			else if(conn.getProtocol().equals("Telnet")){
				String command = this.configuration.getTelnetPath()+" "+conn.getIp();
				try {
					new ProcessBuilder("xterm", "-T", title, "-n", title, "-e",  command).start();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args){
		
		try {
	        UIManager.setLookAndFeel(
	        		UIManager.getSystemLookAndFeelClassName());
	    } 
	    catch (Exception e) {
	       System.out.println("No OS look and feel");
	    }
		try{
			Configuration config = new Configuration("sshtellaunch.conf");
	
			DBReader database = new DBReader(config.getDatabasePath());
			View v = new View(config, database);
			v.setVisible(true);
		}
		catch(Exception e){
			String message = e.getMessage();
			JOptionPane.showMessageDialog(null, message, "Fatal error", JOptionPane.ERROR_MESSAGE);
		}
		
	}

}
