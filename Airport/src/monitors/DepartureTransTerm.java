package monitors;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import Interfaces.DepartureTransTermBDriverInterface;
import Interfaces.DepartureTransTermPassengerInterface;
import airport.Bus;
import airport.Logger;
import airport.Passenger;

public class DepartureTransTerm implements DepartureTransTermPassengerInterface,DepartureTransTermBDriverInterface{
	private final ReentrantLock lock = new ReentrantLock();
	private final Condition busEmpty = lock.newCondition();
	private Bus bus;
	Logger logger;
	
	public DepartureTransTerm(Bus bus, Logger logger) {
		this.bus = bus;
		this.logger = logger;
	}

	public void leaveTheBus(Passenger P) {
		lock.lock();
		try {
			bus.removePassenger(P);
			if(bus.PassengerSize() == 0) {
				busEmpty.signal();
			}
	
		}finally {
			lock.unlock();
		}
	}
	
	public void waitForPassengers() {
		while(!(bus.PassengerSize() == 0)) {
			try {
				busEmpty.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
