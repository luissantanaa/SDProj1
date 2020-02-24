package airport;

public class Plane {
	Passenger[] passengers;
	int planeId;
	Bag[] bags;
	public Plane(Passenger[] passengers, int planeId, Bag[] bags) {
		super();
		this.passengers = passengers;
		this.planeId = planeId;
		this.bags = bags;
	}
	public Passenger[] getPassengers() {
		return passengers;
	}
	public void setPassengers(Passenger[] passengers) {
		this.passengers = passengers;
	}
	public int getPlaneId() {
		return planeId;
	}
	public void setPlaneId(int planeId) {
		this.planeId = planeId;
	}
	public Bag[] getBags() {
		return bags;
	}
	public void setBags(Bag[] bags) {
		this.bags = bags;
	}
}
