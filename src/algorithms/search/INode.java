package algorithms.search;

public interface INode {
	public int getH();	// Cost to goal (Heuristic)
	public int getG();	// Cost from start
	public int getF();	// Total cost

	// For path
	public INode getParent();
	public void setParent(INode parent);
}
