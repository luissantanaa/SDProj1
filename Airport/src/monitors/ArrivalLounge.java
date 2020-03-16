package monitors;



import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import Interfaces.ArrivalLoungeInterfacePassenger;
import Interfaces.ArrivalLoungeInterfacePorter;
import airport.Bag;
import airport.Plane;
import states.StatesPerson;

public class ArrivalLounge implements ArrivalLoungeInterfacePassenger,ArrivalLoungeInterfacePorter{
	private final ReentrantLock lock = new ReentrantLock();
	Plane plane;
	
	public Plane getPlane() {
		return plane;
	}
	public void setPlane(Plane plane) {
		this.plane = plane;
	}
	public ArrivalLounge(){
		
	}
	public StatesPerson whatShouldIDo(List<Bag> bag, boolean dest) {
		if(dest) {
			if(bag.isEmpty()) {
				return StatesPerson.EXITING_THE_ARRIVAL_TERMINAL;
				
			}
			return StatesPerson.AT_THE_LUGGAGE_COLLECTION_POINT;
			
		}else {
			return StatesPerson.AT_THE_ARRIVAL_TRANSFER_TERMINAL;
		}
	}
	
	public boolean takeARest() {
		return plane.getPassengers().size() == 0; 
	}
	
	public Bag collectBag() {
		lock.lock();
		try{	
			if(this.plane.getBags().size() > 0) {
				return plane.getBags().remove();
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
