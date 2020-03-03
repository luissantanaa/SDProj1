package monitors;

import airport.Passenger;
import states.StatesPerson;

public class DepartureArrivalTerm {

	public DepartureArrivalTerm() {
	}

	public void leaveTheBus(Passenger P) {
		P.setState(StatesPerson.AT_THE_DEPARTURE_TRANSFER_TERMINAL);		
	}
}
