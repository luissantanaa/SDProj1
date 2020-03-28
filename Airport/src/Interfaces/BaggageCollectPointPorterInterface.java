package Interfaces;

import airport.Bag;

/**
 * 
 * Interface de acesso do bagageiro ao Monitor Baggage Collection Point
 *
 */

public interface BaggageCollectPointPorterInterface {
	public boolean addBag(Bag bag);
	public void noMoreBags();
}
