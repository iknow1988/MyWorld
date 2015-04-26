import java.awt.Color;

public class SeamCarver {
	private MyImage image;
	private int[][] energyMatrix;
	private int seamNo;

	public SeamCarver(MyImage img, int seamNo) {
		this.seamNo = seamNo;
		this.image = new MyImage(img);
		energyMatrix = new int[img.height()][img.width()];
		for (int x = 0; x < getHeight(); x++) {
			for (int y = 0; y < getWidth(); y++) {
				energyMatrix[x][y] = getEnergy(y, x);
			}
		}
	}

	public SeamCarver(int[][] matrix, int col, int row, int seamNo) {
		this.seamNo = seamNo;
		energyMatrix = new int[row][col];
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				energyMatrix[i][j] = matrix[i][j];
			}
		}
	}

	public MyImage getImage() {
		return image;
	}

	public int getWidth() {
		if (image != null) {
			return image.width();
		} else {
			return energyMatrix[0].length;
		}
	}

	public int getHeight() {
		if (image != null) {
			return image.height();
		} else {
			return energyMatrix.length;
		}
	}

	public int getEnergy(int x, int y) {
		int xDiff;
		int yDiff;
		if (x == 0 || y == 0) {
			xDiff = gradient(image.get(0, y), image.get(x, y));
			yDiff = gradient(image.get(x, 0), image.get(x, y));
		} else if (x == getWidth() - 1 || y == getHeight() - 1) {
			xDiff = gradient(image.get(x - 1, y), image.get(x, y));
			yDiff = gradient(image.get(x, y - 1), image.get(x, y));
		} else {
			xDiff = gradient(image.get(x - 1, y), image.get(x + 1, y));
			yDiff = gradient(image.get(x, y - 1), image.get(x, y + 1));
		}
		return xDiff + yDiff;
	}

	public int[] findSeam() {
		int[] seam = new int[getHeight()];
		int[][] parent = new int[getHeight()][getWidth()];
		int[][] table = new int[getHeight()][getWidth()];
		for (int i = 0; i < getHeight(); i++) {
			for (int j = 0; j < getWidth(); j++) {
				if (i == 0) {
					table[i][j] = energyMatrix[i][j];
					parent[i][j] = -1;
					continue;
				}

				if (j == 0) {
					int a = table[i-1][j];
					int b = table[i-1][j+1];
					int min = Math.min(a, b);
					table[i][j] =min + energyMatrix[i][j];
					if (a == min) {
						parent[i][j] = j;
					} else {
						parent[i][j] = j + 1;
					}
					continue;
				}

				if (j == getWidth() - 1) {
					int a = table[i-1][j];
					int b = table[i-1][j-1];
					int min = Math.min(a, b);
					table[i][j] =min + energyMatrix[i][j];
					if (a == min) {
						parent[i][j] = j;
					} else {
						parent[i][j] = j - 1;
					}
					continue;
				}

				int left = table[i-1][j-1];
				int mid = table[i-1][j];
				int right = table[i-1][j+1];;

				int min = Math.min(Math.min(left, mid), right);
				table[i][j] =min + energyMatrix[i][j];
				if (min == left) {
					parent[i][j] = j - 1;
				} else if (min == mid) {
					parent[i][j] = j;
				} else {
					parent[i][j] = j + 1;
				}
			}
		}

		int min = table[getHeight() - 1][0];
		int best = 0;
		for (int index = 0; index < table[getHeight() - 1].length; index++) {
			if (table[getHeight() - 1][index] < min) {
				min = table[getHeight() - 1][index];
				best = index;
			}
		}

		seam[getHeight() - 1] = best;
		for (int i = getHeight() - 2; i >= 0; i--) {
			seam[i] = parent[i + 1][best];
			best = parent[i + 1][best];
		}
		
		return seam;
	}

	public int[][] removeAllSeam() {
		for (int i = 0; i < this.seamNo; i++) {
			int[] seam = findSeam();
			removeSeam(seam);
		}

		return this.energyMatrix;
	}

	public void removeSeamWithRecomputingEnergyMatrix(int[] seam) {
		MyImage img = new MyImage(getWidth() - 1, getHeight());
		for (int y = 0; y < getHeight(); y++) {
			int k = 0;
			for (int x = 0; x < getWidth(); x++) {
				if (x != seam[y]) {
					if (this.image != null)
						img.set(k, y, image.get(x, y));
					k++;
				}
			}
		}
		if (this.image != null)
			this.image = img;
		energyMatrix = new int[getHeight()][getWidth()];
		if (this.image != null) {
			for (int x = 0; x < getHeight(); x++) {
				for (int y = 0; y < getWidth(); y++) {
					energyMatrix[x][y] = getEnergy(y, x);
				}
			}
		}
	}

	public int[][] removeSeam(int[] seam) {
		MyImage img = new MyImage(getWidth() - 1, getHeight());
		int[][] newMatrix = new int[getHeight()][getWidth() - 1];
		for (int y = 0; y < getHeight(); y++) {
			int k = 0;
			for (int x = 0; x < getWidth(); x++) {
				if (x != seam[y]) {
					if (this.image != null)
						img.set(k, y, image.get(x, y));
					newMatrix[y][k] = this.energyMatrix[y][x];
					k++;
				}
			}
		}
		if (this.image != null)
			this.image = img;

		this.energyMatrix = newMatrix;

		return newMatrix;
	}

	private int gradient(Color a, Color b) {
		int red = a.getRed() - b.getRed();
		int green = a.getGreen() - b.getGreen();
		int blue = a.getBlue() - b.getBlue();
		return red * red + green * green + blue * blue;
	}

	public void transpose() {
		MyImage transposedPicture = new MyImage(image.height(), image.width());
		int[][] newEnergy = new int[image.width()][image.height()];
		for (int i = 0; i < image.width(); i++)
			for (int k = 0; k < image.height(); k++) {
				transposedPicture.set(k, i, image.get(i, k));
				newEnergy[i][k] = energyMatrix[k][i];
			}
		energyMatrix = newEnergy;
		image = transposedPicture;
	}
	
	public void setSeam(int seam){
		this.seamNo = seam;
	}
}