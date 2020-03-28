package Interfaces;

import java.util.List;

import airport.Bag;
import states.StatesPerson;

/**
 * 
 * Interface de acesso do passageiro ao Monitor Arrival Lounge
 *
 */

public interface ArrivalLoungeInterfacePassenger{
	public StatesPerson whatShouldIDo(List<Bag> P, boolean dest);
}
