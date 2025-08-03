package utils;

import java.util.ArrayList;
import java.util.List;

public class State {
    private final List<Integer> list;
    private final List<Integer> indexs;
    private final List<Integer> highLight;

    public State(List<Integer> list, List<Integer> indexs, List<Integer> highLight) {
        this.list = list != null ? new ArrayList<>(list) : new ArrayList<>();
        this.indexs = indexs != null ? new ArrayList<>(indexs) : new ArrayList<>();
        this.highLight = highLight != null ? new ArrayList<>(highLight) : new ArrayList<>();
    }

    public List<Integer> getList() {
        return new ArrayList<>(list);
    }

    public List<Integer> getIndexs() {
        return new ArrayList<>(indexs);
    }

    public List<Integer> getHighLight() {
        return new ArrayList<>(highLight);
    }
}
