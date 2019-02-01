import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/*
 * BFS to traverse the maze.
 */

public class BFSRunner {

	private int fringeSize;

	public List<Block> solveMazeBFS(Block[][] maze, List<Block> nodesTraversed) {
		// Holds all the blocks traversed during the BFS search.
		fringeSize = 0;
		Map<Integer, Block> pathMap = new LinkedHashMap<>();
		boolean solutionFound = false;
		Block b = maze[0][0];
		Queue<Block> list = new LinkedList<>();
		list.add(b);
		fringeSize++;
		b.setVisited(true);
		while (!list.isEmpty()) {
			b = list.remove();
			nodesTraversed.add(b);
			if (b.isEnd()) {
				solutionFound = true;
				break;
			} else {
				for (Block neighbour : getNeighbouringBlocks(b, maze)) {
					if (!neighbour.isVisited() && !neighbour.isBlocked()) {
						neighbour.setVisited(true);
						neighbour.setPreviousId(b.getId());
						pathMap.put(neighbour.getId(), b);
						list.add(neighbour);
						fringeSize++;
					}
				}
			}
		}
		// Holds the actual path from start to goal.
		List<Block> actualPath = new LinkedList<>();
		if (solutionFound) {
			Block block = pathMap.get(b.getId());
			actualPath.add(block);
			while (block.getPreviousId() != -1) {
				Block previousBlock = pathMap.get(block.getId());
				actualPath.add(previousBlock);
				block = previousBlock;
			}
			return actualPath;
		}
		return null;
	}

	public int getFringeSize() {
		return fringeSize;
	}

	private List<Block> getNeighbouringBlocks(Block b, Block[][] maze) {
		List<Block> neighbours = new LinkedList<>();
		// up
		if (b.getxPosition() - 1 >= 0) {
			neighbours.add(maze[b.getxPosition() - 1][b.getyPosition()]);
		}
		// left
		if (b.getyPosition() - 1 >= 0) {
			neighbours.add(maze[b.getxPosition()][b.getyPosition() - 1]);
		}
		// down
		if (b.getxPosition() + 1 < maze.length) {
			neighbours.add(maze[b.getxPosition() + 1][b.getyPosition()]);
		}
		// right
		if (b.getyPosition() + 1 < maze.length) {
			neighbours.add(maze[b.getxPosition()][b.getyPosition() + 1]);
		}
		return neighbours;
	}
}
