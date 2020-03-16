package monitors;

import java.util.concurrent.locks.ReentrantLock;

import Interfaces.DepartureArrivalTermPassengerInterface;
import airport.Bus;
import airport.Passenger;

public class DepartureArrivalTerm implements DepartureArrivalTermPassengerInterface{
	private final ReentrantLock lock = new ReentrantLock();

	private Bus bus;
	
	public DepartureArrivalTerm(Bus bus) {
		this.bus = bus;
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
