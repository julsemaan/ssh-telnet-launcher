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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.SwingConstants;
import javax.swing.JButton;


public class View extends JFrame{
	private JTree tree;
	private DefaultMutableTreeNode root;
	private Hashtable<DefaultMutableTreeNode,Connection> bindings;
	private Configuration configuration;
	private JTextField ipField;
	private JComboBox comboProtocols;
	
	
	public View(Configuration configuration, DBReader database) throws Exception{
		this.configuration = configuration;
		
		getContentPane().setBackground(Color.WHITE);
		
		root = new DefaultMutableTreeNode("root");
		bindings = new Hashtable<DefaultMutableTreeNode,Connection>();
		this.populateTreeWithVector(database.crawlDatabase(), this.root);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panelManualConnection = new JPanel();
		panelManualConnection.setPreferredSize(new Dimension(300, 95));
		getContentPane().add(panelManualConnection, BorderLayout.NORTH);
		panelManualConnection.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel manualConnectionLabel = new JLabel("Manual Connection");
		manualConnectionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		manualConnectionLabel.setPreferredSize(new Dimension(200, 15));
		panelManualConnection.add(manualConnectionLabel);
		
		JPanel ipPanel = new JPanel();
		ipPanel.setPreferredSize(new Dimension(200, 20));
		panelManualConnection.add(ipPanel);
		ipPanel.setLayout(new BoxLayout(ipPanel, BoxLayout.X_AXIS));
		
		JLabel labelIp = new JLabel("IP : ");
		ipPanel.add(labelIp);
		
		ipField = new JTextField();
		ipPanel.add(ipField);
		ipField.setColumns(10);
		ipField.addActionListener(new ManualConnectionListener());
		
		JPanel panel = new JPanel();
		panelManualConnection.add(panel);
		
		String protocols[] = {"SSH", "Telnet"};
		comboProtocols = new JComboBox(protocols);
		panel.add(comboProtocols);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ManualConnectionListener());
		panel.add(btnConnect);
		
		
		
		JPanel panelTree = new JPanel();
		panelTree.setBackground(Color.WHITE);
		panelTree.setMinimumSize(new Dimension(300, 500));
		panelTree.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		tree = new JTree(this.root);
		tree.setAlignmentX(Component.LEFT_ALIGNMENT);
		tree.addMouseListener(new TreeClickListener());
		panelTree.add(tree);
		JScrollPane scrollPane = new JScrollPane(panelTree);
		scrollPane.setPreferredSize(new Dimension(300,450));
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		this.setSize(300,600);
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
				DefaultMutableTreeNode dmtn = new DefaultMutableTreeNode(c.getName());
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
			System.out.println((String)View.this.comboProtocols.getSelectedItem());
			Connection c = new Connection("", View.this.ipField.getText(), (String)View.this.comboProtocols.getSelectedItem());
			View.this.openConnection(c);
		}
	}
	
	private void openConnection(Connection conn){
		//System.out.println(c);
		if(conn != null){
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
						new ProcessBuilder("xterm", "-e",  command).start();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
			else if(conn.getProtocol().equals("Telnet")){
				String command = this.configuration.getTelnetPath()+" "+conn.getIp();
				try {
					new ProcessBuilder("xterm", "-e",  command).start();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args){
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
