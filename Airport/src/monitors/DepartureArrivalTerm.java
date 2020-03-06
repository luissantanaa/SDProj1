package monitors;

import airport.Bus;
import airport.Passenger;
import states.StatesPerson;

public class DepartureArrivalTerm {
	private Bus bus;
	
	public DepartureArrivalTerm(Bus bus) {
		this.bus = bus;
	}

	public void leaveTheBus(Passenger P) {
		if(!bus.removePassenger(P)) {
			//erro
		}
	}
}
