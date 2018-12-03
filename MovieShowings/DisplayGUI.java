import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.Font;

import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.SwingConstants;

public class DisplayGUI {

	private JFrame displayInfo;
	private static JTable displayTable;
	
	//value pass from opening this GUI
	private static boolean isAdmin; //check if admin mode is on
	private static String movieNameS, movieTime, movieRating;
	private static String firstString;
	private static String locationCHAR; //check if from "c" cinema / "m" movie
	
	Object [] col_movie = {"Cinema", "Address", "Time"};
	Object [] col_cinema = {"Movie Name", "Rating", "Time"};
	Object [] col_movieUser = {"Cinema", "Address", "Time"};
	Object [] col_cinemaUser = {"Movie Name","Rating", "Time"};
	protected static DefaultTableModel model = new DefaultTableModel();
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DisplayGUI window = new DisplayGUI(firstString, isAdmin, locationCHAR);
					window.displayInfo.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public DisplayGUI(String firstS, boolean isA, String from) {
		firstString = firstS;
		isAdmin=isA;
		locationCHAR = from;
		initialize(firstString, isAdmin, locationCHAR);
	}
	
	private void initialize(String firstS, boolean isA, String from) //initialize(String cmname (cinema or movie name), String called_from, boolean admin)
	{
		displayInfo = new JFrame();
		displayInfo.setResizable(false);
		displayInfo.setBounds(100, 100, 1165, 607);
		displayInfo.getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 85, 1123, 407);
		displayInfo.getContentPane().add(scrollPane);
		
		displayTable = new JTable(){
			public boolean isCellEditable(int data, int columns) {
				return false;
			}
		};
		displayTable.setRowHeight(30);
		displayTable.setFont(new Font("Tahoma", Font.PLAIN, 20));
		displayTable.setAutoCreateRowSorter(true);
		scrollPane.setViewportView(displayTable);
		
		JButton btn_remove = new JButton("Remove");
		btn_remove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (displayTable.getSelectedRow()>=0){
					int input = messageBox.doMessage("Confirm", "Remove Selected?");
					if(input==0) //yes
					{
						removeSelectedRow(displayTable, model);
						//below is to refresh
						if(locationCHAR == "c")
						{
							clearBox(model);
							database.loadDataToDisplay_C(firstString);
						}
						else if(locationCHAR=="m")
						{
							clearBox(model);
							database.loadDataToDisplay_M(firstString);
						}
					}
					else if(input==1) //no
					{
						//do nothing
					}
				}
				else {
					System.out.println("Nothing was selected to be removed from display.");
				}
			}
		});
		btn_remove.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btn_remove.setBounds(524, 505, 171, 54);
		displayInfo.getContentPane().add(btn_remove);
		
		JLabel mainlbl = new JLabel("Title Label");
		mainlbl.setHorizontalAlignment(SwingConstants.LEFT);
		mainlbl.setForeground(Color.BLUE);
		mainlbl.setFont(new Font("Tahoma", Font.PLAIN, 40));
		mainlbl.setBounds(36, 13, 659, 59);
		displayInfo.getContentPane().add(mainlbl);
		
		//----------
		if(isAdmin==false)
		{
			scrollPane.setBounds(12, 85, 1135, 457);
			btn_remove.setVisible(false);
			
			if(locationCHAR=="c")
			{
				clearBox(model);
				displayInfo.setTitle("Cinema Info"); 
				mainlbl.setText("Cinema: " + firstString);//the cinemaName
				model.setColumnIdentifiers(col_cinemaUser);
				displayTable.setModel(model);
				displayTable.getColumnModel().getColumn(0).setMinWidth(200);
				displayTable.getColumnModel().getColumn(0).setMaxWidth(1000);
				displayTable.getColumnModel().getColumn(0).setPreferredWidth(350);
				displayTable.getColumnModel().getColumn(1).setMinWidth(20);
				displayTable.getColumnModel().getColumn(1).setMaxWidth(100);
				displayTable.getColumnModel().getColumn(1).setPreferredWidth(80);
				//get data from database
				database.loadDataToDisplay_CUser(firstString);
			}
			if(locationCHAR=="m")
			{
				clearBox(model);
				displayInfo.setTitle("Movie Info");//the movie Name
				mainlbl.setText("Movie: "+ firstString);
				model.setColumnIdentifiers(col_movieUser);
				displayTable.setModel(model);
				displayTable.getColumnModel().getColumn(0).setMinWidth(200);
				displayTable.getColumnModel().getColumn(0).setMaxWidth(1000);
				displayTable.getColumnModel().getColumn(0).setPreferredWidth(350);
				displayTable.getColumnModel().getColumn(1).setMinWidth(20);
				displayTable.getColumnModel().getColumn(1).setMaxWidth(100);
				displayTable.getColumnModel().getColumn(1).setPreferredWidth(80);
				//get data from database
				database.loadDataToDisplay_MUser(firstString);
			}
		}
		else if(isAdmin==true)
		{
			btn_remove.setVisible(true);
			
			if(locationCHAR=="c")
			{
				clearBox(model);
				displayInfo.setTitle("Cinema Info"); 
				mainlbl.setText("Cinema: " + firstString);//the cinemaName
				model.setColumnIdentifiers(col_cinema);
				displayTable.setModel(model);
				displayTable.getColumnModel().getColumn(0).setMinWidth(200);
				displayTable.getColumnModel().getColumn(0).setMaxWidth(1000);
				displayTable.getColumnModel().getColumn(0).setPreferredWidth(350);
				displayTable.getColumnModel().getColumn(1).setMinWidth(20);
				displayTable.getColumnModel().getColumn(1).setMaxWidth(100);
				displayTable.getColumnModel().getColumn(1).setPreferredWidth(80);
				//get data from database
				database.loadDataToDisplay_C(firstString);
			}
			if(locationCHAR=="m")
			{
				clearBox(model);
				displayInfo.setTitle("Movie Info");//the movie Name
				mainlbl.setText("Movie: "+ firstString);
				model.setColumnIdentifiers(col_movie);
				displayTable.setModel(model);
				displayTable.getColumnModel().getColumn(0).setMinWidth(200);
				displayTable.getColumnModel().getColumn(0).setMaxWidth(1000);
				displayTable.getColumnModel().getColumn(0).setPreferredWidth(350);
				displayTable.getColumnModel().getColumn(1).setMinWidth(20);
				displayTable.getColumnModel().getColumn(1).setMaxWidth(100);
				displayTable.getColumnModel().getColumn(1).setPreferredWidth(80);
				//get data from database
				database.loadDataToDisplay_M(firstString);
			}
		}
		displayInfo.setVisible(true);
	}
	
	public static void clearBox(DefaultTableModel defaultTableModelObj)
	{
		if (defaultTableModelObj.getRowCount() > 0) 
		{
		    for (int i = defaultTableModelObj.getRowCount() - 1; i > -1; i--) 
		    {
		    	defaultTableModelObj.removeRow(i);
		    }
		}		
	}
	
	public void removeSelectedRow(JTable t, DefaultTableModel m)
	{
		String movieS=null, cinemaS=null, timeS=null;
		int i = t.getSelectedRow();
		if(i>=0)
		{
			if(locationCHAR=="c")
			{
				movieS = (String) m.getValueAt(i, 0);
				cinemaS = firstString;
				timeS = (String) m.getValueAt(i, 2);
				
			}
			else if(locationCHAR=="m")
			{
				movieS = firstString;
				cinemaS = (String) m.getValueAt(i, 0);
				timeS = (String) m.getValueAt(i, 2);
			}
			database.deleteMCT(movieS, cinemaS, timeS);
		}
	}
}