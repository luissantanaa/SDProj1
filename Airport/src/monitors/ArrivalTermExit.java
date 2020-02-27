package monitors;

import airport.Passenger;
import states.StatesPerson;

public class ArrivalTermExit {

	public ArrivalTermExit() {
	}
	
	public void GoHome(Passenger P) {
		P.setState(StatesPerson.EXITING_THE_ARRIVAL_TERMINAL);
	}
}
