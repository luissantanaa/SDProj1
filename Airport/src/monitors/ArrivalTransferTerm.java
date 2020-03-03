package monitors;

import airport.Passenger;
import states.StatesPerson;

public class ArrivalTransferTerm {

	public ArrivalTransferTerm() {
	
	}
	
	public void enterTheBus(Passenger P) {
		P.setState(StatesPerson.TERMINAL_TRANSFER);	
	}

	public void hasDaysWorkEnded() {
		
	}
	
	public void announcingBusBoarding() {
		
	}
}
