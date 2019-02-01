/*
 * Generate maze.
 */
public class MapGenerator {

	protected void generateMaze(Block[][] maze, int dim, double p) {
		double toBeBlocked = dim * dim * p;
		while (Math.floor(toBeBlocked) > 0) {
			for (int i = 0; i < dim; i++) {
				for (int j = 0; j < dim; j++) {
					Block b = null;
					if (maze[i][j] == null || maze[i][j].getNumber() != -1) {
						b = new Block();
					} else {
						continue;
					}
					double blockProbability = b.getPossiblity();
					boolean isFirst = i == 0 && j == 0;
					boolean isLast = i == dim - 1 && j == dim - 1;
					if (isFirst || isLast) {
						b.setStart(isFirst);
						b.setEnd(isLast);
						b.setNumber(1);
					} else if (blockProbability < p && Math.floor(toBeBlocked) > 0) {
						b.setBlocked(true);
						b.setNumber(-1);
						toBeBlocked--;
					}
					b.setxPosition(i);
					b.setyPosition(j);
					b.setId(i * dim + j);
					if (b.getNumber() == -1) {
						b.setIsOCcupied(true);
					}
					maze[i][j] = b;
				}
			}
		}
	}
}
