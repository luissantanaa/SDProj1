package monitors;

import java.util.ArrayList;
import java.util.List;

import Interfaces.BaggageReclaimOfficePassengerInterface;
import airport.Bag;
import airport.Passenger;

public class BaggageReclaimOffice implements BaggageReclaimOfficePassengerInterface {


	public BaggageReclaimOffice() {

	}
	
	public void reportMissingBag(Passenger P) {
		P.goHome();
	}

}
