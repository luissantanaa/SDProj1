package airport;

import java.util.List;


public class Bus {	
	private List<Passenger> passengers;
	private int maxPasse;
	public Bus(List<Passenger> passengers,int MaxPasse) {
		super();
		this.maxPasse = MaxPasse;
		this.passengers = passengers;
	}
	public List<Passenger> getPassengers() {
		return passengers;
	}
	public void setPassengers(List<Passenger> passengers) {
		this.passengers = passengers;
	}
	public int PassengerSize() {
		return this.passengers.size();
	}
	public boolean hasSpace() {
		return this.passengers.size() != this.maxPasse; 
	}
	public boolean addPassenger(Passenger P) {
		if (hasSpace()) {
			this.passengers.add(P);
			return true;
		}
		return false;
	}
	public boolean removePassenger(Passenger P) {
		if (passengers.contains(P)) {
			this.passengers.remove(P);
			return true;
		}
		return false;
	}
	
}
