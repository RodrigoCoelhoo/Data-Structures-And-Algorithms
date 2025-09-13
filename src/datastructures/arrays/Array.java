package datastructures.arrays;

import java.util.ArrayList;

import datastructures.interfaces.IDataStructure;
import javafx.geometry.Bounds;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import utils.DataStructureState;
import utils.DataStructureState.Parameters;

public class Array<T> implements IDataStructure<T> {

    ArrayList<DataStructureState<T>> states = new ArrayList<>();

	private final int size;
	private final T[] array;

	@SuppressWarnings("unchecked")
	public Array(int size) {
		this.size = size;
		this.array = (T[]) new Object[size];
	}

	public T[] getArray() {
		return array;
	}

	public int size() {
		return this.size;
	}
	
	@Override
    public void set(int index, T value) {
		if(index < 0 || index >= size) throw new IndexOutOfBoundsException();
		
		Parameters param = new Parameters();
		param.setObjective(index);
		saveState(this.clone(), param);
		saveState(this, param);
		saveState(this, new Parameters());

		this.array[index] = value;
	}

	@Override
	public T get(int index) {
		if(index < 0 || index >= size) throw new IndexOutOfBoundsException();

		Parameters param = new Parameters();
		param.setObjective(index);
		saveState(this, param);
		saveState(this, param);
		saveState(this, new Parameters());

		return this.array[index];
	}
	
	@Override
	public IDataStructure<T> clone() {
		Array<T> result = new Array<>(this.size);
		
		for(int i = 0; i < this.size; i++) {
			result.getArray()[i] = this.array[i];
		}

		return result;
	}

	@Override
	public void clear() {
		for(int i = 0; i < this.size; i++) {
			this.array[i] = null;
		}
	}

	@Override
	public String info() {
		String 
		result = "An array is a fixed-size data structure that stores elements "
				+ "of the same type in contiguous memory locations. "
				+ "Each element can be accessed directly using its index, "
				+ "making access and update operations very efficient (constant time).\n\n";
		
		result += "However, operations that require shifting elements, such as "
				+ "insertion or deletion at arbitrary positions, are costly "
				+ "because they may involve moving many elements (linear time).\n\n";

		result += "'n' is the number of elements in the array.\n\n";
		
		result += "Access Time Complexity: O(1)\n";
        result += "Search Time Complexity: O(n)\n";
        result += "Insert Time Complexity: O(n)\n";
        result += "Delete Time Complexity: O(n)\n";
		result += "Space Complexity: O(n)\n\n";
		
		
		return result;
	}


	@Override
	public void draw(Pane pane, Parameters param) {
		pane.getChildren().clear();

        double paneWidth = pane.getWidth() > 0 ? pane.getWidth() : 800;
        double paneHeight = pane.getHeight() > 0 ? pane.getHeight() : 600;
		
		double box = 50;
		double space = 4;
		
		double totalWidth = box * this.size + space * (this.size - 1);
		double x = paneWidth/2 - totalWidth/2;
		double y = paneHeight/2 - box - space;

		for(int i = 0; i < this.size; i++) {
			Rectangle rect = new Rectangle(x, y, box, box);
			
			String val = (array[i] != null) ? array[i].toString() : "NULL";
			Text value = new Text(val);
			value.setFont(Font.font("Arial", 16));
			value.setX(x + box / 2);
			value.setY(y + box / 2);
			Bounds bounds = value.getBoundsInLocal();
			value.setX(x + (box - bounds.getWidth()) / 2);
			value.setY(y + (box + bounds.getHeight()) / 2);
			
			Text index = new Text(Integer.toString(i));
			index.setFont(Font.font("Arial", 16));
			Bounds indexBounds = index.getBoundsInLocal();
			index.setX(x + (box - indexBounds.getWidth()) / 2);
			index.setY(y + box + 4 + indexBounds.getHeight());

			x += box + space;

			rect.setStrokeWidth(2);

			Color color = param.getColor(i);
			rect.setFill(color);
			rect.setStroke(Color.BLACK);
			pane.getChildren().addAll(rect, value, index);
		}
	}

	public ArrayList<DataStructureState<T>> getStates() { 
        return this.states; 
    }

    public void clearStates() { 
        this.states = new ArrayList<>(); 
    }

	public void saveState(IDataStructure<T> ds, Parameters param) {
        this.states.add(new DataStructureState<>(ds, param));
    }
}
