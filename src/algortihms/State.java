package algortihms;

import java.util.ArrayList;
import java.util.List;

public class State {
    private final List<Integer> list;
    private final int[] indices;

    public State(List<Integer> list, int[] indices) {
        this.list = new ArrayList<>(list);
        this.indices = indices != null ? indices.clone() : null;
    }

    public List<Integer> getList() {
        return new ArrayList<>(list);
    }

    public int[] getIndices() {
        return indices != null ? indices.clone() : null;
    }

    @Override
    public String toString() {
        return "List: " + list + "\n Active Indexs: " + (indices != null ? "[" + indices[0] + ", " + indices[1] + "]" : "null");
    }
}
