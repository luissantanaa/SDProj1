package monitors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import Interfaces.DepartureTermEntrancePassengerInterface;
import airport.Bag;
import airport.Passenger;

public class DepartureTermEntr implements DepartureTermEntrancePassengerInterface {
	private int nPassengers=0;
	private final ReentrantLock lock = new ReentrantLock();
	private final Condition plock = lock.newCondition(); 
	private List<Passenger> pList;
	
	public DepartureTermEntr() {
			nPassengers = 0;
			pList = new ArrayList<>();
	}
	
	public void setNPassengers(int n) {
		lock.lock();
		try {
			
			nPassengers = n;
	
		}finally {
			plock.signalAll();
			lock.unlock();
		}
	}
	public boolean prepareNextLeg(Passenger P) {
		lock.lock();
		try {
			if(!pList.contains(P)) {
				pList.add(P);
			}
			
			while(nPassengers==0 || nPassengers!=pList.size()) {
				
				plock.await();
			}
		
			nPassengers--;
			pList.remove(P);
			return true;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			plock.signalAll();
			lock.unlock();
		}
		return false;
	}
}
