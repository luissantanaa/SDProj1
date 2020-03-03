package monitors;

import airport.Passenger;
import states.StatesPerson;

public class ArrivalLounge {

	public ArrivalLounge() {
	}
	public void whatShouldIDo(Passenger P) {
		if(P.isDest()) {
			if(P.HasBag()) {
				 P.setState(StatesPerson.AT_THE_LUGGAGE_COLLECTION_POINT);
			}else {
				P.setState(StatesPerson.EXITING_THE_ARRIVAL_TERMINAL);
			}
		}else{
			P.setState(StatesPerson.AT_THE_ARRIVAL_TRANSFER_TERMINAL);
		}
	}
}
