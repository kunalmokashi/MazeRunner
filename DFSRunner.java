import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/*
 * DFS to traverse the maze.
 */
public class DFSRunner {

	protected List<Block> solveMazeDFS(Block[][] maze, List<Block> nodesTraversed) {
		Map<Integer, Block> pathMap = new HashMap<>();
		boolean solutionFound = false;
		Block b = maze[0][0];
		Stack<Block> stack = new Stack<>();
		stack.push(b);
		while (!stack.empty()) {
			b = stack.pop();
			b.setVisited(true);
			nodesTraversed.add(b);
			if (b.isEnd()) {
				solutionFound = true;
				break;
			} else {
				for (Block neighbour : getNeighbouringBlocks(b, maze)) {
					if (!neighbour.isVisited() && !neighbour.isBlocked()) {
						neighbour.setPreviousId(b.getId());
						pathMap.put(neighbour.getId(), b);
						stack.push(neighbour);
					}
				}
			}
		}
		if (solutionFound) {
			// Holds the actual path from start to goal.
			List<Block> actualPath = new LinkedList<>();
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
