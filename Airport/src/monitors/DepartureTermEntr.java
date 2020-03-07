package monitors;

import Interfaces.DepartureTermEntrancePassengerInterface;
import airport.Passenger;
import states.StatesPerson;

public class DepartureTermEntr implements DepartureTermEntrancePassengerInterface {

	public DepartureTermEntr() {
	}
	public void prepareNextLeg(Passenger P) {
		P.prepareNextLeg();
	}
}
