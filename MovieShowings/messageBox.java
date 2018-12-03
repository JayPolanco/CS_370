import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
public class messageBox {

	public messageBox() {
	}
	
	//GENERAL PURPOSE METHODS
	public static void doMessage(String s) {
		UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("ARIAL",Font.PLAIN,35)));
		JLabel message = new JLabel(s);
		message.setFont(new Font("Times New Roman", Font.PLAIN, 40));
		JOptionPane.showMessageDialog(null, message);
	}
	
	public static int doMessage(String x, String y) {
		UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("ARIAL",Font.PLAIN,35)));
		JLabel message = new JLabel(x);
		message.setFont(new Font("Times New Roman", Font.PLAIN, 40));
		return JOptionPane.showConfirmDialog(null, message, y, JOptionPane.YES_NO_OPTION);
	}
		
}