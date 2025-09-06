package utils;

import java.util.HashSet;
import java.util.Set;

import datastructures.interfaces.IDataStructure;
import javafx.scene.layout.Pane;

public class DataStructureState<T> {
	private final IDataStructure<T> dataStructure;
	private final Parameters param;

	public DataStructureState(IDataStructure<T> dataStructure, Parameters param) {
		this.dataStructure = dataStructure.clone();
		this.param = param;
	}

	public IDataStructure<T> getDataStructure() {
		return dataStructure;
	}

	public void draw(Pane visualContainer) {
		dataStructure.draw(visualContainer, this.param);
	}

	public static class Parameters {
		private String path = "";							// For trees "LLRLRLR"
		private Set<Integer> indexs = new HashSet<>(); 		// For list
		private int objective = -1; 					// If true last element of indexs or path are the objective
		private Set<Integer> invsible = new HashSet<>(); 	// Indexs of the hidden nodes (For insert, remove animations) 

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public Set<Integer> getIndexs() {
			return this.indexs;
		}

		public void setIndexs(Set<Integer> indexs) {
			this.indexs = indexs;
		}

		public int getObjective() {
			return this.objective;
		}

		public void setObjective(int objective) {
			this.objective = objective;
		}

		public Set<Integer> getInvsible() {
			return this.invsible;
		}

		public void setInvsible(Set<Integer> invsible) {
			this.invsible = invsible;
		}
	}
}
