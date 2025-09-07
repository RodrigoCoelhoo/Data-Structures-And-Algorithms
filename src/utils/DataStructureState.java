package utils;

import java.util.HashSet;
import java.util.Set;

import datastructures.interfaces.IDataStructure;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class DataStructureState<T> {
	private final IDataStructure<T> dataStructure;
	private final Parameters param;

	public DataStructureState(IDataStructure<T> dataStructure, Parameters param) {
		this.dataStructure = dataStructure;
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
		private int objective = -1; 						// If true last element of indexs or path are the objective
		private Set<Integer> invsible = new HashSet<>(); 	// Indexs of the hidden nodes (For insert, remove animations)
		private Set<Integer> failure = new HashSet<>(); 	// Indexs of the failure nodes (Failed search, ...) 

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

		public Set<Integer> getFailure() {
			return failure;
		}

		public void setFailure(Set<Integer> failure) {
			this.failure = failure;
		}

		public Color getColor(int index) {
			if(this.invsible.contains(index)) {
                return Color.TRANSPARENT;
            } 
            else if(this.objective == index) {
                return  Color.LIGHTGREEN;
            }
			else if(this.failure.contains(index)) {
				return Color.TOMATO;
			}
            else if(this.getIndexs().contains(index)) {
                return Color.YELLOW;
            } else {
                return Color.LIGHTBLUE;
            }
		}
	}
}
