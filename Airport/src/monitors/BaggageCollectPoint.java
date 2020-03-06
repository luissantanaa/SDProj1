package monitors;

import java.util.ArrayList;
import java.util.List;

import airport.Bag;

public class BaggageCollectPoint {
	List<Bag> bags;
	public BaggageCollectPoint() {
		bags = new ArrayList<Bag>();
	}
	public void carryItToAppStore(Bag bag){
		bags.add(bag);
	}
}
