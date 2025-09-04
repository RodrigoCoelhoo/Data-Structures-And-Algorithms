package utils;

import datastructures.interfaces.IDataStructure;

public class DataStructureState<T> {
	private IDataStructure<T> dataStructure;
	private int highlightIndex;
	private String highlightPath;

	public DataStructureState(IDataStructure<T> dataStructure, int highlightIndex, String highlightPath) {
		this.dataStructure = dataStructure.clone();
		this.highlightIndex = highlightIndex;
		this.highlightPath = highlightPath;
	}

	public IDataStructure<T> getDataStructure() {
		return dataStructure;
	}
	
	public int getHighlightIndex() {
		return highlightIndex;
	}

	public String getHighlightPath() {
		return highlightPath;
	}
}
