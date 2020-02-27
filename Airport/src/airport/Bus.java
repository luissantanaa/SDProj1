package airport;

import states.StatesBusD;

public class Bus {
	
	Passenger[] passengers;
	StatesBusD state;
	public Bus(Passenger[] passengers) {
		super();
		this.passengers = passengers;
		this.state =  StatesBusD.PARKING_AT_THE_ARRIVAL_TERMINAL;
	}
	public Passenger[] getPassengers() {
		return passengers;
	}
	public void setPassengers(Passenger[] passengers) {
		this.passengers = passengers;
	}
	public StatesBusD getState() {
		return state;
	}
	public void setState(StatesBusD state) {
		this.state = state;
	}
}
