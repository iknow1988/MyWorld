import java.io.IOException;

public class Game implements SudokuConstants {
	private HostGraphics hostGraphics;
	private GuestGraphics guestGraphics;
	private SudokuGrid grid;
	private Player hostPlayer;
	private Player guestPlayer;

	public Game(Player hostPlayer) {
		this.hostPlayer = hostPlayer;
		this.hostPlayer.start();
		this.grid = new SudokuGrid(DEFAULT_DIFFICULTY);
		this.hostGraphics = new HostGraphics(this.grid, this.hostPlayer, this);
	}

	public void setHostPlayer(Player hostPlayer) throws Exception {
		this.hostPlayer = hostPlayer;
		this.hostPlayer.start();
	}

	public void setGuestPlayer(Player guestPlayer) throws Exception {
		this.guestPlayer = guestPlayer;
	}

	public void setGrid() {
		this.grid = new SudokuGrid(8);
	}

	public void setGrid(SudokuGrid grid) {
		this.grid = grid;
	}

	public void setHostGraphics() {
		this.hostGraphics = new HostGraphics(this.grid, this.hostPlayer, this);
	}

	public void setGuestGraphics() {
		this.guestGraphics = new GuestGraphics(this.grid, this.guestPlayer,
				this.hostGraphics, this);
	}

	public Player getGuestPlayer() {
		return this.guestPlayer;
	}

	public Player getHostPlayer() {
		return this.hostPlayer;
	}

	public SudokuGrid getGrid() {
		return this.grid;
	}

	public void singlePlay() {
		hostGraphics.setLocation(0, 150);
		hostGraphics.setVisible(true);
	}

	public void doublePlay() {
		hostGraphics.run();
		guestGraphics.setLocation(APPLICATION_WIDTH + 5, 150);
		guestGraphics.setVisible(true);
		guestGraphics.start();
	}

	public GuestGraphics getGuestGraphics() {
		return this.guestGraphics;
	}

	public HostGraphics getHostGraphics() {
		return this.hostGraphics;
	}

	public void exit() {
		try {
			if (this.getGuestPlayer() != null) {
				this.guestPlayer.output.close();
				this.guestPlayer.input.close();
				this.guestPlayer.getGuestSocket().close();
				this.getGuestGraphics().setVisible(false);
				this.guestPlayer = null;
			}
			if (this.getHostPlayer().output != null) {
				this.getHostPlayer().output.close();
				this.getHostPlayer().input.close();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			this.hostPlayer.getHostSocket().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.hostGraphics.setVisible(false);
	}

}
