package airport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;


public class Bus {	
	private List<Passenger> passengers;
	private int maxPasse;
	private final ReentrantLock lock = new ReentrantLock();
	
	
	
	public Bus(int MaxPasse) {
		super();
		this.maxPasse = MaxPasse;
		this.passengers = new ArrayList<Passenger>();
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
		
		lock.lock();
		try {
			
			if (hasSpace()) {
				this.passengers.add(P);
				return true;
			}
		}finally {
			lock.unlock();
		}

		return false;
	}
	public boolean removePassenger(Passenger P) {
		lock.lock();
		try {
			if (passengers.contains(P)) {
				this.passengers.remove(P);
				return true;
			}
		}finally {
			lock.unlock();
		}
		return false;
	}
	
	public String toPrint() {
		String s = "";
		switch(passengers.size()) {
			case 0:
				s = " - - - ";
				break;
			case 1:
				s = " "+ this.passengers.get(0).getPId() + " - - ";
				break;
			case 2:
				s = " "+ this.passengers.get(0).getPId() +" "+this.passengers.get(1).getPId()+ " - ";
				break;
			case 3:
				s = " "+ this.passengers.get(0).getPId() + " " + this.passengers.get(1).getPId() +" "+ this.passengers.get(2).getPId();
				break;
			default:
				
		}
		return s;
	}
	
}
