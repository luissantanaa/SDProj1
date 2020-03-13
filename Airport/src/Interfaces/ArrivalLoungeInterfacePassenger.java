package Interfaces;

import java.util.List;

import airport.Bag;
import airport.Passenger;
import states.StatesPerson;

public interface ArrivalLoungeInterfacePassenger{
	public StatesPerson whatShouldIDo(List<Bag> P, boolean dest);
}
