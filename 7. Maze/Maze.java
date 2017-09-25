import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Maze {
	private static String INPUT_FILE_NAME = "task.bmp";
	private static String OUTPUT_FILE_NAME = "key";

	public static void main(String[] args) throws IOException {
		BufferedImage inputImage = ImageIO.read(new File(INPUT_FILE_NAME));
		int height = inputImage.getHeight();
		int width = inputImage.getWidth();

		Node[][] nodes = new Node[height][width];
		Set<Node> unsettled = new HashSet<>();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Color color = new Color(inputImage.getRGB(x, y));
				int red = color.getRed();
				int green = color.getGreen();
				int blue = color.getBlue();
				nodes[y][x] = new Node(y, x, red + green + blue);
				unsettled.add(nodes[y][x]);
			}
		}

		nodes[0][0].setDistance(nodes[0][0].getWeight());
		while (!unsettled.isEmpty()) {
			Node minNode = extractMin(unsettled);
			unsettled.remove(minNode);
			int minY = minNode.getY();
			int minX = minNode.getX();
			if (minX < width - 1) {
				relax(minNode, nodes[minY][minX + 1]);
			}
			if (minX > 0) {
				relax(minNode, nodes[minY][minX - 1]);
			}
			if (minY < height - 1) {
				relax(minNode, nodes[minY + 1][minX]);
			}
			if (minY > 0) {
				relax(minNode, nodes[minY - 1][minX]);
			}
		}

		LinkedList<Node> shortestPath = getPath(nodes[height - 1][width - 1]);
		if (shortestPath == null) {
			throw new IllegalStateException("Path doesn't exist");
		}

		byte[] result = new byte[32];
		result[0] = (byte) nodes[height - 1][width - 1].getDistance();
		int currentResultIndex = 1;
		FileOutputStream outputFile = new FileOutputStream(OUTPUT_FILE_NAME);
		for (Node node : shortestPath) {
			result[currentResultIndex++] = (byte) node.getWeight();
			if (currentResultIndex >= 32) {
				break;
			}
		}
		outputFile.write(result);
		outputFile.close();
	}

	private static Node extractMin(Set<Node> from) {
		Node min = null;
		for (Node node : from) {
			if (min == null) {
				min = node;
			} else {
				if (node.getDistance() < min.getDistance()) {
					min = node;
				}
			}
		}
		return min;
	}

	private static void relax(Node from, Node to) {
		int oldDistance = to.getDistance();
		int newDistance = from.getDistance() + to.getWeight();
		if (oldDistance > newDistance) {
			to.setDistance(newDistance);
			to.setPrevNode(from);
		}
	}

	private static LinkedList<Node> getPath(Node to) {
		LinkedList<Node> path = new LinkedList<>();
		Node current = to;
		if (current.getPrevNode() == null) {
			return null;
		}
		path.add(current);
		while (current.getPrevNode() != null) {
			current = current.getPrevNode();
			path.add(current);
		}
		Collections.reverse(path);
		return path;
	}

	private static class Node {
		private int x;
		private int y;
		private int weight;
		private int distance;
		private Node prevNode;

		public Node(int y, int x, int weight) {
			this.y = y;
			this.x = x;
			this.weight = weight;
			this.distance = Integer.MAX_VALUE;
			this.prevNode = null;
		}

		public void setDistance(int distance) {
			this.distance = distance;
		}

		public void setPrevNode(Node prevNode) {
			this.prevNode = prevNode;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public int getWeight() {
			return weight;
		}

		public int getDistance() {
			return distance;
		}

		public Node getPrevNode() {
			return prevNode;
		}
	}
}
