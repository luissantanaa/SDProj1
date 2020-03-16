package monitors;

import java.util.ArrayList;
import java.util.List;

import Interfaces.BaggageReclaimOfficePassengerInterface;
import airport.Logger;
import airport.Passenger;

public class BaggageReclaimOffice implements BaggageReclaimOfficePassengerInterface {
	List<Passenger> passengerReclaim; 
	Logger logger;
	public BaggageReclaimOffice(Logger logger) {
		passengerReclaim = new ArrayList<Passenger>(); 
		this.logger = logger;
	}
	
	public void reportMissingBag(Passenger P) {
		
		passengerReclaim.add(P);
	}

}
