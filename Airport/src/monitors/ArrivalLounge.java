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
	int size;
	Logger logger;
	
	
	public Plane getPlane() {
		return plane;
	}
	public void setPlane(Plane plane) {
		
		this.plane = plane;
		size = 0;
	}
	public ArrivalLounge(Logger logger){
		size = 0;
		this.logger = logger;
	}
	public StatesPerson whatShouldIDo(List<Bag> bag, boolean dest) {
		
		lock.lock();
		try {
			if(dest) {
				if(bag.isEmpty()) {
					return StatesPerson.EXITING_THE_ARRIVAL_TERMINAL;
					
				}
				return StatesPerson.AT_THE_LUGGAGE_COLLECTION_POINT;
				
			}else {
				return StatesPerson.AT_THE_ARRIVAL_TRANSFER_TERMINAL;
			}
			
		}finally {
			
			size++;
			if(size==6) {
				
				porterc.signal();
			}
			lock.unlock();
		}	
	}
	
	public boolean takeARest() {
		lock.lock();
		try {	
			while(plane==null || size!=6) {
				porterc.await();
			}
			size=0;
			if(plane.getBags().size()>0) {
				return true;
			}
			
			
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
					return plane.getBag();
				}else {
					return null;
				}
			}finally {
				
				lock.unlock();
			}
			
	}

}

class Airplane{
	int passengserSize;
	int planeID;
}
