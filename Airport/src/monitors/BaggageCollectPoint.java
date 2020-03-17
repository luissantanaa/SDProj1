package monitors;

import java.util.ArrayList;

import Interfaces.BaggageCollectPointPassengerInterface;
import Interfaces.BaggageCollectPointPorterInterface;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.lang.Math; 

import airport.Bag;
import airport.Logger;


public class BaggageCollectPoint implements BaggageCollectPointPorterInterface,BaggageCollectPointPassengerInterface  {
	private final ReentrantLock lock = new ReentrantLock();
	 private final Condition bagNotAdded = lock.newCondition(); 

	List<Bag> bags;
	Logger logger;
	
	public BaggageCollectPoint(Logger logger) {
		bags = new ArrayList<Bag>();
		this.logger = logger;
	}
	
	public boolean addBag(Bag bag){
		//notify para acordar passageiro
		lock.lock();
		try {
			if(Math.random() >= 0) { //0.25
				bags.add(bag);	
				
				
				return true;
			}
		}finally {
			logger.incrementCB();
			logger.toPrint();
			bagNotAdded.signal();
			lock.unlock();
		}
		return false;
	}
	
	public boolean collectBag(Bag bag) {
		lock.lock();
		try {	
			while(!bags.contains(bag)) {
				bagNotAdded.await();
			}
				if(bags.contains(bag)) {
					bags.remove(bag);
			
					return true;
				}else {
					return false;
				}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			logger.decrementCB();
			logger.toPrint();
			lock.unlock();
		}	
		return false;
	}
}
