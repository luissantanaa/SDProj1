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
	
	public void run() {
		while() {
			switch(this.state) {
				case PARKING_AT_THE_ARRIVAL_TERMINAL:
					//faltam cenas
					this.goToDepartureTerminal();
					break;
				case DRIVING_FORWARD:
					this.parkTheBusAndLetPassOff();
					break;
				
				case PARKING_AT_THE_DEPARTURE_TERMINAL:
					//faltam cenas
					this.goToArrivalTerminal();	
					break;
									
				case DRIVING_BACKWARD:
					this.parkTheBus();
					break;
					
				default:
			}
		}
	}
	public void goToArrivalTerminal() {
		this.state = StatesBusD.DRIVING_BACKWARD;
	}
	public void parkTheBus() {
		this.state = StatesBusD.PARKING_AT_THE_ARRIVAL_TERMINAL;
	}
	public void goToDepartureTerminal() {
		this.state = StatesBusD.DRIVING_FORWARD;
	}
	public void parkTheBusAndLetPassOff() {
		this.state = StatesBusD.PARKING_AT_THE_DEPARTURE_TERMINAL;
	}
}
