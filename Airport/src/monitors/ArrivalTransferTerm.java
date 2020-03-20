package monitors;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import Interfaces.ArrivalTransferTermPassengerInterface;
import airport.Bus;
import airport.BusDriver;
import airport.Logger;
import airport.Passenger;

public class ArrivalTransferTerm implements ArrivalTransferTermPassengerInterface{
	private final ReentrantLock lock = new ReentrantLock();
	private final Condition busFull = lock.newCondition();
	private Bus bus;
	private Logger logger;
	
	public ArrivalTransferTerm(Bus bus, Logger logger) {
		this.bus = bus;
		this.logger = logger;
	}
	
	
	
	
	public boolean enterTheBus(Passenger P) {
		lock.lock();
		try {	
			if(bus.addPassenger(P)) {
				if(!bus.hasSpace()) {
					busFull.signal();
				}
				return true;
			}
		}finally {
			lock.unlock();
		}
		return false;
			
	}
	
	public void BusNotFull() {
		lock.lock();
		try {
			while(bus.hasSpace()) {
				busFull.await();
			}
		}catch(InterruptedException e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
	}

	public void hasDaysWorkEnded(BusDriver BD) {

	}
	
	public void announcingBusBoarding() {
		
	}
}
