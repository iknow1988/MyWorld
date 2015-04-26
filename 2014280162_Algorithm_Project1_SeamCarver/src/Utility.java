import javax.swing.JOptionPane;

public class Utility {

	public static void showErrorDialog(String msg) {
		JOptionPane.showMessageDialog(null, msg, "Error",
				JOptionPane.ERROR_MESSAGE);
	}
	
	public static void showSuccessDialog(String msg) {
		JOptionPane.showMessageDialog(null, msg, "Success",
				JOptionPane.INFORMATION_MESSAGE);
	}
}
