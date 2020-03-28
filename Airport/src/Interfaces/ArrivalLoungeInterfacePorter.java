package Interfaces;

import airport.Bag;

/**
 * 
 * Interface de acesso do bagageiro ao Monitor Arrival Louge
 *
 */

public interface ArrivalLoungeInterfacePorter {
	public boolean takeARest();
	public Bag collectBag();
	 public boolean itWasLast();
}
