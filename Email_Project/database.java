import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.swing.table.DefaultTableModel;

public class database {
	public static Connection myConn;
	public static LinkedList<String> ListOfUsers = new LinkedList<String>();
	
	private static User currentUser;
	private String nextUserInTable;
	//Gets connection to a database with the name email
	final static String url = "jdbc:mysql://:3306/email?useSSL=false";
	private static PreparedStatement delete, stmt, update;
	private static ResultSet rs;
	
	public static Connection Connect(){
		/*all mySQL url for java require the format jdbc:mysql://
		followed by the host(IP assigned to database):port(3306)/database name*/
		
		try {
				
			//connect to database, xxxxxxx represents the poassword for the DB
			myConn = DriverManager.getConnection(url, "XXX", "XXXXX");
			return myConn;
		}
		catch (SQLException ex) {
			throw new RuntimeException("Couldn't connect to database");
		}

	}
	
	public static LinkedList<String> getAllUser() {
		String sql = "SELECT * FROM email.users";
		try {
			stmt = Connect().prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next()){
				
				ListOfUsers.add(rs.getString("username") + rs.getString("password"));;
			}
			return ListOfUsers;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				Connect().close();
				stmt.close();
				rs.close();
			} catch (SQLException e) {
				//e.printStackTrace();
			}
		}
		return null;
	}
	
	public static void insertUser(User s) {
		try {
			String insert = "Insert into email.users " +
					"VALUES(?,?)";
			PreparedStatement stmt = Connect().prepareStatement(insert);
			
			//filling in question marks
			stmt.setString(1, s.getUserName());
			stmt.setString(2, s.getPassword());
			
			stmt.executeUpdate();
			
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				Connect().close();
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
	}
	
	public static void fillBox(User u, DefaultTableModel model_box, String locationCHAR) {
//		Homepage.clearBox(model_box);
		/*model_box can be model_inbox, model_sent, model_draft
		 * I for model_inbox
		 * S for model_sent 
		 * D for model_draft
		*/
		if (locationCHAR.equals("I")) {
			String sql = "SELECT * FROM email.`" + u.getUserName() + "`" +
					" WHERE location = '" + locationCHAR +
					"' ORDER BY emailNum ASC";
			String fromEmail, subject, timestamp, body;        //*******KARL********
			int primeKey;
			
			try {
				stmt = Connect().prepareStatement(sql);
				rs = stmt.executeQuery();
				
				while(rs.next()){
					//Retrieve the 4 values in the current row of the table, then move to the next row
					fromEmail = rs.getString("From");
					subject = rs.getString("subject");
					timestamp = rs.getString("time");
					primeKey = rs.getInt("emailNum");
					body = rs.getString("body");                
					
					Homepage.add_row(model_box, fromEmail, subject, timestamp, primeKey, body); 
					
				}
			} catch (SQLException e) {
				//e.printStackTrace();
			}
			finally {
				try {
					Connect().close();
					stmt.close();
					rs.close();
				} catch (SQLException e) {
					//e.printStackTrace();
				}
			}
		}
		
		else if (locationCHAR.equals("S") || locationCHAR.equals("D")){
			String sql = "SELECT * FROM email.`" + u.getUserName() + "`" +
					" WHERE location = '" + locationCHAR +
					"' ORDER BY emailNum ASC";
			String toEmail, subject, timestamp, body; 
			int primeKey;
			
			try {
				stmt = Connect().prepareStatement(sql);
				rs = stmt.executeQuery();
				
				while(rs.next()){
					//Retrieve the 4 values in the current row of the table, then move to the next row
					toEmail = rs.getString("to");
					subject = rs.getString("subject");
					timestamp = rs.getString("time");
					primeKey = rs.getInt("emailNum");
					body = rs.getString("body");
					
					Homepage.add_row(model_box, toEmail , subject, timestamp, primeKey, body); 
					
					
				}
			} catch (SQLException e) {
				//e.printStackTrace();
			}
			finally {
				try {
					Connect().close();
					stmt.close();
					rs.close();
				} catch (SQLException e) {
					//e.printStackTrace();
				}
			}
			
		}

	}//End of fillBox
	
	public static void deleteEmail(int pk, String from){
		String del = "DELETE FROM email.`" + from + "`" +
				" WHERE emailNum = " + pk;
	try {
		delete = Connect().prepareStatement(del);
		delete.executeUpdate();
		
	} catch (SQLException ex) {
		//ex.printStackTrace();
	}
	finally {
		try {
			Connect().close();
			delete.close();
		} catch (SQLException e) {
			//e.printStackTrace();
		}
	}
		
	}
	
	public static void addEmail(String to, String from, String subject, String body, String location, String needTimeStamp) {
		
