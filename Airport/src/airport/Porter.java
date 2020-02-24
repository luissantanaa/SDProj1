package airport;

public class Porter {
	enum states {WAITING_FOR_A_PLANE_TO_LAND, AT_THE_PLANES_HOLD, AT_THE_LUGGAGE_BELT_CONVEYOR, AT_THE_STOREROOM};
	Bag b;
	int planeId;
	states state;
	public Porter(Bag b, int planeId) {
		super();
		this.b = b;
		this.planeId = planeId;
		this.state = states.WAITING_FOR_A_PLANE_TO_LAND;
	}
	public Bag getB() {
		return b;
	}
	public void setB(Bag b) {
		this.b = b;
	}
	public int getPlaneId() {
		return planeId;
	}
	public void setPlaneId(int planeId) {
		this.planeId = planeId;
	}
	public states getState() {
		return state;
	}
	public void setState(states state) {
		this.state = state;
	}
	
}
