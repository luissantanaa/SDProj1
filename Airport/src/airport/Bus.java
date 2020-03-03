package airport;

import java.util.List;


public class Bus {	
	List<Passenger> passengers;
	public Bus(List<Passenger> passengers) {
		super();
		this.passengers = passengers;
	}
	public List<Passenger> getPassengers() {
		return passengers;
	}
	public void setPassengers(List<Passenger> passengers) {
		this.passengers = passengers;
	}
	
}
