package monitors;

import java.util.concurrent.locks.ReentrantLock;

import Interfaces.DepartureArrivalTermPassengerInterface;
import airport.Bus;
import airport.Logger;
import airport.Passenger;

public class DepartureArrivalTerm implements DepartureArrivalTermPassengerInterface{
	private final ReentrantLock lock = new ReentrantLock();

	private Bus bus;
	Logger logger;
	
	public DepartureArrivalTerm(Bus bus, Logger logger) {
		this.bus = bus;
		this.logger = logger;
	}

	public void leaveTheBus(Passenger P) {
		lock.lock();
		try {
			bus.removePassenger(P);
	
		}finally {
			lock.unlock();
		}
	}
}
