package monitors;

import java.util.ArrayList;

import Interfaces.BaggageCollectPointPassengerInterface;
import Interfaces.BaggageCollectPointPorterInterface;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.lang.Math; 

import airport.Bag;
import airport.Logger;


public class BaggageCollectPoint implements BaggageCollectPointPorterInterface,BaggageCollectPointPassengerInterface  {
	private final ReentrantLock lock = new ReentrantLock();

	List<Bag> bags;
	Logger logger;
	
	public BaggageCollectPoint(Logger logger) {
		bags = new ArrayList<Bag>();
		this.logger = logger;
	}
	
	public boolean addBag(Bag bag){
		//lock e notify para acordar passageiro
		lock.lock();
		try {
			if(Math.random() > 0.25) {
				logger.incrementCB();
				logger.toPrint();
				bags.add(bag);			
				return true;
			}
		}finally {
			lock.unlock();
		}
		return false;
	}
	
	public boolean collectBag(List<Bag> bag) {
		lock.lock();
		try {	
			for(int i=0; i<bag.size();i++) {
				if(bags.contains(bag.get(i))) {
					logger.decrementCB();
					logger.toPrint();
					bags.remove(bag.get(i));
				}else {
					return false;
				}
			}
		}finally {
			lock.unlock();
		}	
		return true;
	}
}
