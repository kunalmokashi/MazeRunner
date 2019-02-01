import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/*
 * Generate multiple mazes and run A-Star, DFS, BFS on it to determine if the
 * generated maze is harder than the previous one. 
 * Once we have the final maze, we can do analysis with the algorithms A-Star, 
 * DFS, BFS based on the factors like number of explored nodes, fringe size,
 * length of the path to the goal.
 */

public class HardMazeGenerator {
	public static void main(String args[]) {
		HardMazeGenerator hardMazeGenerator = new HardMazeGenerator();
		int dim = 80;
		Block[][] hardMaze = new Block[dim][dim];
		hardMaze = hardMazeGenerator.generate(dim, 0.3, "AStar");
		List<Block> nodesTraversed = new LinkedList<>();
		List<Integer> nodesExpanded = new LinkedList<>();
		List<Block> astarPath = AStar.calculate(hardMaze, hardMaze[0][0], hardMaze[dim - 1][dim - 1], "Manhattan",
				nodesExpanded);
		DrawMaze.draw(hardMaze, null, nodesTraversed, astarPath, "A-Star");

	}

	public Block[][] generate(int dim, double p, String algorithm) {
		Block[][] hardMaze = new Block[dim][dim];
		MapGenerator mg = new MapGenerator();
		HardMazeGenerator hardmg = new HardMazeGenerator();

		// Hard maze in terms of the length of the shortest path.
		if (algorithm.equals("AStar")) {
			int difficulty = 0;
			int failTimes = 0;
			int generation = 0;
			Block[][] parent = new Block[dim][dim];
			Block[][] child = new Block[dim][dim];
			List<Block> firstPath = new ArrayList<>();
			List<Integer> nodeExpanded = new ArrayList<>();
			mg.generateMaze(parent, dim, p);
			firstPath = AStar.calculate(parent, parent[0][0], parent[dim - 1][dim - 1], "Manhattan", nodeExpanded);
			// make sure the first map is always solvable.
			while (firstPath == null) {
				parent = new Block[dim][dim];
				mg.generateMaze(parent, dim, p);
				nodeExpanded = new ArrayList<>();
				firstPath = AStar.calculate(parent, parent[0][0], parent[dim - 1][dim - 1], "Manhattan", nodeExpanded);
			}
			// born a child
			while (failTimes < 200) {
				child = hardmg.born(parent, dim, p);
				int childPathSize = 0;
				List<Block> parentPath = new ArrayList<>();
				nodeExpanded = new ArrayList<>();
				parentPath = AStar.calculate(parent, parent[0][0], parent[dim - 1][dim - 1], "Manhattan", nodeExpanded);
				List<Block> childPath = new ArrayList<>();
				List<Integer> nodeExpandedChild = new ArrayList<>();
				childPath = AStar.calculate(child, child[0][0], child[dim - 1][dim - 1], "Manhattan",
						nodeExpandedChild);
				if (childPath != null) {
					childPathSize = childPath.size();
				} else {
					failTimes++;
					continue;
				}
				difficulty = nodeExpandedChild.size() - nodeExpanded.size();
				// child is not harder than parents.do nothing.
				if (difficulty <= 0) {
					failTimes++;
				}
				// child is harder
				else {
					System.out.println(generation);
					System.out.println(parentPath.size());
					System.out.println(nodeExpanded.size());
					failTimes = 0;
					generation++;
					parent = child;
				}
			}
			hardMaze = parent;
			List<Block> finalPath = new ArrayList<>();
			finalPath = AStar.calculate(parent, parent[0][0], parent[dim - 1][dim - 1], "Manhattan", nodeExpanded);
			System.out.println("generation:" + generation);
			System.out.println("Path length:" + finalPath.size());
		} else if (algorithm.equals("DFS")) {
			DFSRunner runner = new DFSRunner();
			int difficulty = 0;
			int failTimes = 0;
			int generation = 0;
			Block[][] parent = new Block[dim][dim];
			Block[][] child = new Block[dim][dim];
			List<Block> firstPath = new ArrayList<>();
			List<Block> nodeExpanded = new ArrayList<>();
			mg.generateMaze(parent, dim, p);
			firstPath = runner.solveMazeDFS(parent, nodeExpanded);
			// make sure the first map is always solvable.
			while (firstPath == null) {
				parent = new Block[dim][dim];
				nodeExpanded = new ArrayList<>();
				mg.generateMaze(parent, dim, p);
				firstPath = runner.solveMazeDFS(parent, nodeExpanded);

			}
			int parentSize = nodeExpanded.size();
			// born a child
			Block[][] finalMaze = null;
			while (failTimes < 10000) {
				child = hardmg.born(parent, dim, p);
				List<Block> nodeExpandedChild = new ArrayList<>();
				List<Block> childPath = runner.solveMazeDFS(child, nodeExpandedChild);
				if (childPath == null) {
					failTimes++;
					continue;
				}
				difficulty = nodeExpandedChild.size() - parentSize;
				// child is not harder than parents.do nothing.
				if (difficulty <= 0) {
					failTimes++;
				}
				// child is harder
				else {
					System.out.println(generation);
					System.out.println(parentSize);
					failTimes = 0;
					generation++;
					parent = child;
					finalMaze = new Block[dim][dim];
					finalMaze = child;
					parentSize = nodeExpandedChild.size();
				}
			}
			hardMaze = finalMaze;
			nodeExpanded = new ArrayList<>();
			List<Block> path = runner.solveMazeDFS(hardMaze, nodeExpanded);
			System.out.println("generation:" + generation);
			System.out.println("Nodes Expanded Last pass :" + parentSize);
		} else if (algorithm.equalsIgnoreCase("BFS")) {
			BFSRunner runner = new BFSRunner();
			int difficulty = 0;
			int failTimes = 0;
			int generation = 0;
			Block[][] parent = new Block[dim][dim];
			Block[][] child = new Block[dim][dim];
			List<Block> firstPath = new ArrayList<>();
			List<Block> nodeExpanded = new ArrayList<>();
			mg.generateMaze(parent, dim, p);
			firstPath = runner.solveMazeBFS(parent, nodeExpanded);
			int fringeSize = runner.getFringeSize();
			// make sure the first map is always solvable.
			while (firstPath == null) {
				parent = new Block[dim][dim];
				nodeExpanded = new ArrayList<>();
				mg.generateMaze(parent, dim, p);
				firstPath = runner.solveMazeBFS(parent, nodeExpanded);
			}
			int parentFringeSize = fringeSize;
			// born a child
			while (failTimes < 20000) {
				child = hardmg.born(parent, dim, p);
				List<Block> nodeExpandedChild = new ArrayList<>();
				List<Block> childPath = runner.solveMazeBFS(child, nodeExpandedChild);
				int fringeSizeChild = runner.getFringeSize();
				if (childPath == null) {
					failTimes++;
					continue;
				}
				difficulty = fringeSizeChild - parentFringeSize;
				// child is not harder than parents.do nothing.
				if (difficulty <= 0) {
					failTimes++;
				}
				// child is harder
				else {
					System.out.println(generation);
					System.out.println(parentFringeSize);
					failTimes = 0;
					generation++;
					parent = child;
					parentFringeSize = fringeSizeChild;
				}
			}
			hardMaze = parent;
			System.out.println("generation:" + generation);
			System.out.println("Fringe Size:" + parentFringeSize);
		}
		return hardMaze;
	}

	public Block[][] born(Block[][] maze1, int dim, double p) {
		Block[][] child = new Block[dim][dim];
		Block[][] maze2 = new Block[dim][dim];
		MapGenerator mg = new MapGenerator();
		mg.generateMaze(maze2, dim, p);
		Random rand = new Random();
		int spliter = rand.nextInt(dim - 2) + 1;
		for (int column = 0; column < spliter; column++)
			for (int row = 0; row < dim; row++) {
				child[row][column] = maze1[row][column];
			}
		for (int column = spliter; column < dim; column++)
			for (int row = 0; row < dim; row++) {
				child[row][column] = maze2[row][column];
			}
		return child;
	}

}
