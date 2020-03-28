package monitors;

import java.util.ArrayList;
import java.util.List;

import Interfaces.BaggageReclaimOfficePassengerInterface;
import airport.Logger;
import airport.Passenger;

/**
 * 
 * Monitor Baggage Reclaim Office
 *
 */

public class BaggageReclaimOffice implements BaggageReclaimOfficePassengerInterface {
	private List<Passenger> passengerReclaim; 
	//private Logger logger;
	
	//construtor
	/**
	 * 
	 * passengerReclaim é uma lista de passageiros que perderam malas
	 */
	public BaggageReclaimOffice(Logger logger) {
		passengerReclaim = new ArrayList<Passenger>(); 
		//this.logger = logger;
	}
	
	//reportar mala perdida, adiciona passageiro a lista de passageiros que perderam malas
	/**
	 * usada por passageiros que perderam mala, são adicionados a lista
	 */
	public void reportMissingBag(Passenger P) {
		
		passengerReclaim.add(P);
	}

}
