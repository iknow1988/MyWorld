import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MyFileReader {
	private String fileName;
	public int rowNum;
	public int colNum;
	public int seamNum;
	public int[][] energyMatrix;

	public MyFileReader(String fileName) {
		this.fileName = fileName;
	}

	public MyFileReader(String fileName, int rowNum, int colNum) {
		this.fileName = fileName;
		this.rowNum = rowNum;
		this.colNum = colNum;
	}

	public void readFile() throws IOException, FileNotFoundException {
		BufferedReader br = new BufferedReader(new FileReader(this.fileName));
		String line = br.readLine();
		String[] matDimension = line.split(" ");
		this.rowNum = Integer.parseInt(matDimension[0]);
		this.colNum = Integer.parseInt(matDimension[1]);
		this.seamNum = Integer.parseInt(matDimension[2]);
		this.energyMatrix = new int[rowNum][colNum];
		int row = 0;
		int col = 0;
		while ((line = br.readLine()) != null) {
			String[] entries = line.split(" ");
			for (String entry : entries) {
				energyMatrix[row][col] = Integer.parseInt(entry);
				col++;
			}
			//System.out.println(line);
			row++;
			col = 0;
		}
		br.close();
	}

	public void checkFile() throws IOException, FileNotFoundException {
		BufferedReader br = new BufferedReader(new FileReader(this.fileName));
		String line;
		this.energyMatrix = new int[rowNum][colNum];
		int row = 0;
		int col = 0;
		while ((line = br.readLine()) != null) {
			String[] entries = line.split(" ");
			for (String entry : entries) {
				energyMatrix[row][col] = Integer.parseInt(entry);
				col++;
				this.colNum = col;
			}
			//System.out.println(line);
			row++;
			col = 0;
		}
		br.close();
	}
}
