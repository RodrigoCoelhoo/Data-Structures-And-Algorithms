package algorithms.interfaces;

import java.util.List;

public interface ILayout {
    INode getInitialNode();
    boolean isGoal(INode node);
    List<INode> getSuccessors(INode node);
}