package class10;

import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class Code01_UnionFind {

	public static class Node<V> {
		V value;

		public Node(V v) {
			value = v;
		}
	}

	public static class UnionSet<V> {
		public HashMap<V, Node<V>> nodes;
		public HashMap<Node<V>, Node<V>> parents;
		public HashMap<Node<V>, Integer> sizeMap;

		public UnionSet(List<V> values) {
			nodes = new HashMap<>();
			parents = new HashMap<>();
			sizeMap = new HashMap<>();
			for (V cur : values) {
				Node<V> node = new Node<>(cur);
				nodes.put(cur, node);
				parents.put(node, node);
				sizeMap.put(node, 1);
			}
		}

		// 从点cur开始，一直往上找，找到不能再往上的代表点，返回
		public Node<V> findFather(Node<V> cur) {
			Stack<Node<V>> path = new Stack<>();
			while (cur != parents.get(cur)) {
				path.push(cur);
				cur = parents.get(cur);
			}
			// cur头节点
			while (!path.isEmpty()) {
				parents.put(path.pop(), cur);
			}
			return cur;
		}

		public boolean isSameSet(V a, V b) {
			if (!nodes.containsKey(a) || !nodes.containsKey(b)) {
				return false;
			}
			return findFather(nodes.get(a)) == findFather(nodes.get(b));
		}

		public void union(V a, V b) {
			if (!nodes.containsKey(a) || !nodes.containsKey(b)) {
				return;
			}
			Node<V> aHead = findFather(nodes.get(a));
			Node<V> bHead = findFather(nodes.get(b));
			if (aHead != bHead) {
				int aSetSize = sizeMap.get(aHead);
				int bSetSize = sizeMap.get(bHead);
				Node<V> big = aSetSize >= bSetSize ? aHead : bHead;
				Node<V> small = big == aHead ? bHead : aHead;
				parents.put(small, big);
				sizeMap.put(big, aSetSize + bSetSize);
				sizeMap.remove(small);
			}
		}
	}

}


class Solution {
	/**
	 * @thought：并查集
	 * @date: 1/20/2020 9:59 PM
	 * @Execution info：6ms 击败 27% 的j，MB 击败 7.28% 的j
	 * @Asymptotic Time Complexity：O()
	 */
	public int numIslands(char[][] grid) {
		if(grid.length == 0 || grid == null)  return 0;
		int ans=0;
		UF uf = new UF(grid);
		// 目标是将所有相邻的岛屿连通起来，最后求uf的连通分量个数
		int rows = grid.length;
		int cols = grid[0].length;

		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				if(grid[i][j] == '1') {

					grid[i][j] = '0'; // 标记为访问过
					int u = i-1, d = i+1, l = j-1, r = j+1;
					int t = i*cols + j;

					if(u >= 0 && grid[u][j] == '1')
						uf.union(t, u*cols + j);

					if(d < rows && grid[d][j] == '1')
						uf.union(t, d*cols + j);

					if(l >= 0 && grid[i][l] == '1')
						uf.union(t, i*cols + l);

					if(r < cols && grid[i][r] == '1')
						uf.union(t, i*cols + r);
				}

		return uf.count;
	}

	class UF {
		private int[] parent;
		private int[] rank;
		private int count;  // 连通分量
		public UF(char[][] grid) {
			count = 0;
			int rows = grid.length;
			int cols = grid[0].length;
			parent = new int[rows * cols];
			rank = new int[rows * cols];

			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					int t = i*cols + j;
					rank[t] = 0; // 以(i,j)为根的树的深度暂时为0；
					// 如果该点时岛屿，则初始化根
					if(grid[i][j] == '1') {
						parent[t] = t;  // 为了让下标不重复
						++count;        // 连通分量增加1
					}
				}
			}
		}

		public int getCount() {return count;}

		// 找到结点p对应的组
		public int find(int p) {
			if(p < 0 || p > parent.length)
				throw new IllegalArgumentException("p is out of bound");
			// 路径压缩
			while(p != parent[p]) {
				parent[p] = parent[parent[p]];
				p = parent[p];
			}
			return p;
		}

		public boolean isConnected(int p, int q) {
			return find(p) == find(q);
		}

		public void union(int p, int q) {
			int pRootID = find(p);
			int qRootID = find(q);

			if(pRootID == qRootID)  return;

			if(rank[pRootID] > rank[qRootID])
				parent[qRootID] = pRootID;
			else if(rank[pRootID] < rank[qRootID])
				parent[pRootID] = qRootID;
			else {
				parent[pRootID] = qRootID;
				rank[qRootID]++;  // 深度加一
			}
			count--;
		}
	}
}