package monitors;

import Interfaces.DepartureArrivalTermPassengerInterface;
import airport.Bus;
import airport.Passenger;
import states.StatesPerson;

public class DepartureArrivalTerm implements DepartureArrivalTermPassengerInterface{
	private Bus bus;
	
	public DepartureArrivalTerm(Bus bus) {
		this.bus = bus;
	}

	public void leaveTheBus(Passenger P) {
		bus.removePassenger(P);
	}
}
