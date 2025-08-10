package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import algorithms.interfaces.ILayout;
import algorithms.interfaces.INode;

public class State {
    private final List<Integer> list;
    private final List<Integer> indexs;
    private final List<Integer> highLight;

    private final GridSnapshot snapshot;

    public State(List<Integer> list, List<Integer> indexs, List<Integer> highLight) {
        this.list = list != null ? new ArrayList<>(list) : new ArrayList<>();
        this.indexs = indexs != null ? new ArrayList<>(indexs) : new ArrayList<>();
        this.highLight = highLight != null ? new ArrayList<>(highLight) : new ArrayList<>();

        this.snapshot = null;
    }

    public State(ILayout layout, Set<INode> openSet, Set<INode> closedSet, Set<INode> highlight) {
        this.list = null;
        this.indexs = null;
        this.highLight = null;

        this.snapshot = new GridSnapshot(layout, openSet, closedSet, highlight);
    }

    public State(GridSnapshot snapshot) {
        this.list = null;
        this.indexs = null;
        this.highLight = null;
        
        this.snapshot = snapshot;
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

    public GridSnapshot getSnapshot() { return this.snapshot; }
}