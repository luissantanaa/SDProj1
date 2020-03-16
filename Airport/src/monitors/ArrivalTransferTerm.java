package monitors;

import java.util.concurrent.locks.ReentrantLock;

import Interfaces.ArrivalTransferTermPassengerInterface;
import airport.Bus;
import airport.BusDriver;
import airport.Passenger;

public class ArrivalTransferTerm implements ArrivalTransferTermPassengerInterface{
	private final ReentrantLock lock = new ReentrantLock();
	private Bus bus;
	
	
	public ArrivalTransferTerm(Bus bus) {
		this.bus = bus;
	}
	
	
	
	
	public boolean enterTheBus(Passenger P) {
		lock.lock();
		try {	
			if(bus.addPassenger(P)) {
				if(!bus.hasSpace()) {
					//notify bus driver
				}
				return true;
			}
		}finally {
			lock.unlock();
		}
		return false;
			
	
	}

	public void hasDaysWorkEnded(BusDriver BD) {

	}
	
	public void announcingBusBoarding() {
		
	}
}
