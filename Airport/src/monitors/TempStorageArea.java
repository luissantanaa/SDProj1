package monitors;

import java.util.ArrayList;
import java.util.List;

import airport.Bag;

public class TempStorageArea {
	List<Bag> bags;
	public TempStorageArea() {
		bags = new ArrayList<Bag>();
	}
	public void carryItToAppStore(Bag bag){
		bags.add(bag);
	}
}