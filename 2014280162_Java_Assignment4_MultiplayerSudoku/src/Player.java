import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Player extends Thread {
	private boolean host;
	private ServerSocket hostSocket;
	private Socket guestSocket;
	public BufferedReader input;
	public PrintWriter output;
	public SudokuPanel sudoku;

	public Player(int port) throws IOException {
		this.host = true;
		hostSocket = null;
		hostSocket = new ServerSocket(port);

	}

	public Player(int port, String IPAddress) throws UnknownHostException,
			IOException {
		this.host = false;
		guestSocket = new Socket(IPAddress, port);
		output = new PrintWriter(guestSocket.getOutputStream(), true);
		input = new BufferedReader(new InputStreamReader(
				guestSocket.getInputStream()));

	}

	public boolean isHost() {
		return this.host;
	}

	public Socket getGuestSocket() {
		return this.guestSocket;
	}

	public ServerSocket getHostSocket() {
		return this.hostSocket;
	}

	public void run() {
		while (true) {
			try {
				guestSocket = null;
				try {
					guestSocket = hostSocket.accept();
				} catch (Exception ex) {
					System.out.println("Error." + ex);
				}
				input = new BufferedReader(new InputStreamReader(
						guestSocket.getInputStream()));
				output = new PrintWriter(guestSocket.getOutputStream(), true);
			} catch (Exception ex) {
				System.out.println("Error: in server client socket" + ex);
			}
		}
	}
}
