package airport;

public class Bus {
	enum states {PARKING_AT_THE_ARRIVAL_TERMINAL, DRIVING_FORWARD, PARKING_AT_THE_DEPARTURE_TERMINAL, DRIVING_BACKWARD};
	Passenger[] passengers;
	states state;
	public Bus(Passenger[] passengers) {
		super();
		this.passengers = passengers;
		this.state =  states.PARKING_AT_THE_ARRIVAL_TERMINAL;
	}
	public Passenger[] getPassengers() {
		return passengers;
	}
	public void setPassengers(Passenger[] passengers) {
		this.passengers = passengers;
	}
	public states getState() {
		return state;
	}
	public void setState(states state) {
		this.state = state;
	}
}
