package monitors;

import java.util.ArrayList;

import Interfaces.BaggageCollectPointPassengerInterface;
import Interfaces.BaggageCollectPointPorterInterface;
import java.util.List;

import airport.Bag;
import airport.Passenger;

public class BaggageCollectPoint implements BaggageCollectPointPorterInterface,BaggageCollectPointPassengerInterface  {
	List<Bag> bags;
	
	public BaggageCollectPoint() {
		bags = new ArrayList<Bag>();
	}
	
	public void carryItToAppStore(Bag bag){
		bags.add(bag); 
	}
	
	public boolean collectBag(Passenger P) {
		for(int i=0; i<P.getB().size();i++) {
			if(bags.contains(P.getB().get(i))) {
				bags.remove(P.getB().get(i));
			}else {
				P.reportMissingBag();
				return false;
			}
		}
		
		P.goHome();
		return true;
	}
	
}
