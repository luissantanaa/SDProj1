package monitors;



import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import Interfaces.ArrivalLoungeInterfacePassenger;
import Interfaces.ArrivalLoungeInterfacePorter;
import airport.Bag;
import airport.Logger;
import airport.Plane;
import states.StatesPerson;

public class ArrivalLounge implements ArrivalLoungeInterfacePassenger,ArrivalLoungeInterfacePorter{
	private final ReentrantLock lock = new ReentrantLock();
	private final Condition porterc = lock.newCondition(); 
	Plane plane;
	Logger logger;
	public Plane getPlane() {
		return plane;
	}
	public void setPlane(Plane plane) {
		this.plane = plane;
		
	}
	public ArrivalLounge(Logger logger){
		
		this.logger = logger;
		
	}
	public StatesPerson whatShouldIDo(List<Bag> bag, boolean dest) {
		lock.lock();
		try {
			if(plane.isEmpty()) {
				porterc.signal();
			}
			if(dest) {
				if(bag.isEmpty()) {
					return StatesPerson.EXITING_THE_ARRIVAL_TERMINAL;
					
				}
				return StatesPerson.AT_THE_LUGGAGE_COLLECTION_POINT;
				
			}else {
				return StatesPerson.AT_THE_ARRIVAL_TRANSFER_TERMINAL;
			}
			
		}finally {
			lock.unlock();
		}	
	}
	
	public boolean takeARest() {
		lock.lock();
		try {	
			while(plane==null || !plane.isEmpty()) {
				porterc.await();
			}
			return true;
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}	
		return false;
	}
	
	public Bag collectBag() {
			lock.lock();
			try {
				
				if(this.plane.getBags().size() > 0) {
					logger.toPrint();
					return plane.getBag();
				}
			}finally {
				lock.unlock();
			}
			
		
		return null;
	}

}

class Airplane{
	int passengserSize;
	int planeID;
}
