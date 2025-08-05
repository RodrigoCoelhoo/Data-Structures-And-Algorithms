package algorithms.search;

public interface INode {
	// Cost to goal (Heuristic)
	public int getH();
	
	// Cost from start
	public int getG();	
	public void setG(int g);

	// Total cost
	public int getF();	

	// For path
	public INode getParent();
	public void setParent(INode parent);
}
