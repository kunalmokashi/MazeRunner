
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/*
 * A-Star to traverse the maze.(Manhattan & Euclidean heuristics)
 */

public class AStar {
	public static List<Block> calculate(Block[][] map, Block start, Block des, String heuristicMathod,
			List<Integer> nodesExpanded) {
		List<Block> path = new ArrayList<>();
		HashMap<Integer, Block> directory = new HashMap<>();
		for (int i = 0; i < map.length; i++)
			for (int j = 0; j < map[0].length; j++) {
				directory.put(i * map.length + j, map[i][j]);
			}
		List<Integer> openSet = new ArrayList<>();
		List<Integer> closeSet = new ArrayList<>();
		try {
			start.setF(heuristicValue(start, des, heuristicMathod));
		} catch (Exception e) {
			e.printStackTrace();
		}

		Block current = start;
		openSet.add(start.getId());
		// keep going until the openSet is empty.
		while (!openSet.isEmpty()) {
			// get the next best option.
			int lowest = 0;
			for (int i = 1; i < openSet.size(); i++) {
				if ((directory.get(openSet.get(i))).getF() < (directory.get(openSet.get(lowest))).getF()) {
					lowest = i;
				}
				if (directory.get(openSet.get(i)).getF() == directory.get(openSet.get(lowest)).getF()) {
					if (directory.get(openSet.get(i)).getG() > directory.get(openSet.get(lowest)).getG()) {
						lowest = i;
					}
				}
			}
			current = directory.get(openSet.get(lowest));
			nodesExpanded.add(current.getId());
			closeSet.add(openSet.get(lowest));
			openSet.remove(lowest);

			if (current.getId() == des.getId()) {
				Block temp = directory.get(current.getId());
				path.add(temp);

				while (temp.getPreviousId() != -1) {
					Block previous = directory.get(temp.getPreviousId());
					path.add(previous);
					temp = previous;
				}

				if (temp.getId() != start.getId()) {

					return null;
				}
				return path;
			}

			ArrayList<Block> neighbors = AStar.getNeighbors(current, map);
			for (Block neighbor : neighbors) {
				if (closeSet.contains(neighbor.getId())) {
					// do nothing
				} else {
					if (openSet.contains(neighbor.getId())) {
						if (current.getG() + 1 < neighbor.getG())
							neighbor.setG(current.getG() + 1);
					} else {
						openSet.add(neighbor.getId());
					}
					neighbor.setG(current.getG() + 1);
					try {
						neighbor.setF(neighbor.getG() + heuristicValue(neighbor, des, heuristicMathod));
					} catch (Exception e) {
						e.printStackTrace();
					}
					neighbor.setPreviousId(current.getId());
					directory.replace(neighbor.getId(), neighbor);
				}
			}
		}
		return null;

	}

	public static ArrayList<Block> getNeighbors(Block current, Block[][] map) {
		ArrayList<Block> neighbors = new ArrayList<>();
		int currentX = current.getxPosition();
		int currentY = current.getyPosition();
		int dim = map.length;
		if (currentX < dim - 1) {
			neighbors.add(map[currentX + 1][currentY]);
		}
		if (currentX > 0) {
			neighbors.add(map[currentX - 1][currentY]);
		}
		if (currentY < dim - 1) {
			neighbors.add(map[currentX][currentY + 1]);
		}
		if (currentY > 0) {
			neighbors.add(map[currentX][currentY - 1]);
		}
		for (Iterator<Block> it = neighbors.iterator(); it.hasNext();) {
			Block block = it.next();
			if (block.getIsOCcupied())
				it.remove();
		}

		return neighbors;
	}

	public static int heuristicValue(Block a, Block b, String heuristicMathod) throws Exception {
		if (heuristicMathod == "Euclidean")
			return (int) Math.floor(Math.pow(
					Math.pow(a.getxPosition() - b.getxPosition(), 2) + Math.pow(a.getyPosition() - b.getyPosition(), 2),
					0.5));
		else if (heuristicMathod == "Manhattan")
			return Math.abs(a.getxPosition() - b.getxPosition()) + Math.abs(a.getyPosition() - b.getyPosition());
		else
			throw new Exception("Illegal heuristic method argument");
	}

}
