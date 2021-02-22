package class10;

import java.util.ArrayList;

// 点结构的描述  A  0
public class Node {
	public int value;
	public int in; // 出度：
	public int out; // 出度：邻居个数
	public ArrayList<Node> nexts; // 直接邻居 ->
	public ArrayList<Edge> edges;

	public Node(int value) {
		this.value = value;
		in = 0;
		out = 0;
		nexts = new ArrayList<>();
		edges = new ArrayList<>();
	}
}
