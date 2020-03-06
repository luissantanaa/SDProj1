package monitors;

import airport.Bus;
import airport.BusDriver;
import airport.Passenger;
import states.StatesBusD;
import states.StatesPerson;

public class ArrivalTransferTerm {
	private Bus bus;
	public ArrivalTransferTerm(Bus bus) {
		this.bus = bus;
	}
	
	public void enterTheBus(Passenger P) {
		if(bus.addPassenger(P)) {
			
		}else{
			
		}
	}

	public void hasDaysWorkEnded(BusDriver BD) {

	}
	
	public void announcingBusBoarding() {
		
	}
}
