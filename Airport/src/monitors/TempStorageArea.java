package monitors;

import java.util.ArrayList;
import java.util.List;

import airport.Bag;

public class TempStorageArea {
	List<Bag> bags;
	public TempStorageArea() {
		bags = new ArrayList<Bag>();
	}
	public boolean addBag(Bag bag){
		if(Math.random() > 0.25) {
			bags.add(bag);
			return true;
		} 
		return false;
	}
}
