package monitors;

import airport.Passenger;
import states.StatesPerson;

public class DepartureTermEntr {

	public DepartureTermEntr() {
	}
	public void prepareNextLeg(Passenger P) {
		P.setState(StatesPerson.ENTERING_THE_DEPARTURE_TERMINAL);			
	}
}
