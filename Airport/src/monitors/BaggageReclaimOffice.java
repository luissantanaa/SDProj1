package monitors;

import java.util.ArrayList;
import java.util.List;

import Interfaces.BaggageReclaimOfficePassengerInterface;
import airport.Passenger;

public class BaggageReclaimOffice implements BaggageReclaimOfficePassengerInterface {
	List<Passenger> passengerReclaim; 

	public BaggageReclaimOffice() {
		passengerReclaim = new ArrayList<Passenger>(); 
	}
	
	public void reportMissingBag(Passenger P) {
		
		passengerReclaim.add(P);
	}

}
