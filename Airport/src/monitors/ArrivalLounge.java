package monitors;



import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import Interfaces.ArrivalLoungeInterfaceBDriver;
import Interfaces.ArrivalLoungeInterfacePassenger;
import Interfaces.ArrivalLoungeInterfacePorter;
import airport.Bag;
import airport.Logger;
import airport.Plane;
import states.StatesPerson;

public class ArrivalLounge implements ArrivalLoungeInterfacePassenger,ArrivalLoungeInterfacePorter, ArrivalLoungeInterfaceBDriver{
	private final ReentrantLock lock = new ReentrantLock();
	private final Condition porterc = lock.newCondition(); 
	private final Condition busDriver = lock.newCondition(); 
	
	
	private boolean lastF;
	private Plane plane;
	private int size;
	private Logger logger;
	private int sizeBus;
	
	

	
	public Plane getPlane() {
		return plane;
	}
	public void setPlane(Plane plane) {
		this.plane = plane;
		this.logger.setBagSizes(this.getPlane().getBags().size());
		size = 0;
		sizeBus = 0;
	}
	public ArrivalLounge(Logger logger){
		size = 0;
		sizeBus = 0;
		lastF = false;
		this.logger = logger;
	}
	
	
	// Work day ended
	public void lastFlight() {
		lock.lock();
		try {
			lastF = true;
		}finally {
			porterc.signal();
			lock.unlock();
		}
	
	}
	
	
	public boolean itWasLast() {
		return lastF;
	}
	
	
	public StatesPerson whatShouldIDo(List<Bag> bag, boolean dest) {
		
		lock.lock();
		try {
			if(dest) {
				logger.incPassDest();
				if(bag.isEmpty()) {
					return StatesPerson.EXITING_THE_ARRIVAL_TERMINAL;
					
				}
				return StatesPerson.AT_THE_LUGGAGE_COLLECTION_POINT;
				
			}else {
				logger.incPassTransit();
				return StatesPerson.AT_THE_ARRIVAL_TRANSFER_TERMINAL;
			}
			
		}finally {			
			size++;
			if(!dest) {
				sizeBus ++;
			}
			
			if(size==6) {
				porterc.signal();
				busDriver.signal();
			}else if(sizeBus == 3) {
					busDriver.signalAll();
			}
			lock.unlock();
		}	
	}
	
	public boolean takeARest() {
		lock.lock();
		try {	
			while( (size!=6 && !lastF) || (plane.getBags().size()==0 && !lastF)) {
				porterc.await();
			}
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
	
	public int goCollectPassengers() {
		lock.lock();
		try {
			while(sizeBus<3 && size<6) {
				busDriver.await();
			}	
			
			if(sizeBus>=3) {
				sizeBus-=3;
				return 3;
			}else if(sizeBus!=0){
				int sizeb = sizeBus;
				sizeBus=0;
				return sizeb;
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
		return -1;
	}

}