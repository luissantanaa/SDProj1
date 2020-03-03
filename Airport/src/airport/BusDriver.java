package airport;

import states.StatesBusD;

public class BusDriver {
	StatesBusD state;
	
	public BusDriver(StatesBusD state) {
		super();
		this.state =  StatesBusD.PARKING_AT_THE_ARRIVAL_TERMINAL;
	}
	public StatesBusD getState() {
		return state;
	}
	public void setState(StatesBusD state) {
		this.state = state;
	}
}
