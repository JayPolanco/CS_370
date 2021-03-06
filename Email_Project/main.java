import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*Project goal: To create a fully functional email GUI, using
a database and JDBC to store and retrieve data such as email messages and 
accounts info,*/
class main{
	
	public static void main(String[] args) throws IOException{
		//Creates the users table if the table doesn't exist (this table stores usernames and passwords.
		String sql = "CREATE TABLE IF NOT EXISTS users" +
				" (username VARCHAR(50) NOT NULL, " +
				" password VARCHAR(20) NOT NULL, " +
				" PRIMARY KEY (username))";
		try {
			PreparedStatement stmt = database.Connect().prepareStatement(sql);
			stmt.executeUpdate();
			stmt.close();
			//have to use this for data modification queries
		} catch (SQLException e) {
			System.out.println("Error conecting to DB");
			e.printStackTrace();
		}
		
		loginregisterGUI firstWindow = new loginregisterGUI();

	}

}