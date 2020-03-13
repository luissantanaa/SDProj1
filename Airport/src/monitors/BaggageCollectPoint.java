package monitors;

import java.util.ArrayList;

import Interfaces.BaggageCollectPointPassengerInterface;
import Interfaces.BaggageCollectPointPorterInterface;
import java.util.List;
import java.lang.Math; 

import airport.Bag;
import airport.Passenger;

public class BaggageCollectPoint implements BaggageCollectPointPorterInterface,BaggageCollectPointPassengerInterface  {
	List<Bag> bags;
	
	public BaggageCollectPoint() {
		bags = new ArrayList<Bag>();
	}
	
	public boolean addBag(Bag bag){
		//lock e notify para acordar passageiro
		if(Math.random() > 0.25) {
			bags.add(bag);
			return true;
		} 
		return false;
	}
	
	public boolean collectBag(List<Bag> bag) {
		for(int i=0; i<bag.size();i++) {
			if(bags.contains(bag.get(i))) {
				bags.remove(bag.get(i));
			}else {
				return false;
			}
		}
		return true;
	}
}
