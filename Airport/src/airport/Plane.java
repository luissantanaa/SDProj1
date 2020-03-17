package airport;

import java.util.Queue;

public class Plane {
	Queue<Passenger> passengers;
	int planeId;
	Queue<Bag> bags;
	public Plane(Queue<Passenger> passengers, int planeId, Queue<Bag> bags) {
		super();
		this.passengers = passengers;
		this.planeId = planeId;
		this.bags = bags;
	}
	public Queue<Passenger> getPassengers() {
		return passengers;
	}
	public void setPassengers(Queue<Passenger> passengers) {
		this.passengers = passengers;
	}
	
	public Passenger removePassenger() {
		return this.passengers.remove();
	}
	public boolean isEmpty(){
		return this.passengers.size()==0;
	}
	
	public int getPlaneId() {
		return planeId;
	}
	public void setPlaneId(int planeId) {
		this.planeId = planeId;
	}
	public Queue<Bag> getBags() {
		return bags;
	}
	public Bag getBag(){
		return bags.remove();
	}
	public void setBags(Queue<Bag> bags) {
		this.bags = bags;
	}
}
