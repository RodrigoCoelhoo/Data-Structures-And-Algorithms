package algortihms;

import java.util.ArrayList;
import java.util.List;

public class State {
    private final List<Integer> list;
    private final List<Integer> indexs;

    public State(List<Integer> list, List<Integer> indexs) {
        this.list = list != null ? new ArrayList<>(list) : new ArrayList<>();
        this.indexs = indexs != null ? new ArrayList<>(indexs) : new ArrayList<>();
    }

    public List<Integer> getList() {
        return new ArrayList<>(list);
    }

    public List<Integer> getIndexs() {
        return new ArrayList<>(indexs);
    }
}
