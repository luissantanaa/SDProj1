package airport;

import states.StatesPorter;

public class Porter {
	
	Bag b;
	int planeId;
	StatesPorter state;
	public Porter(Bag b, int planeId) {
		super();
		this.b = b;
		this.planeId = planeId;
		this.state = StatesPorter.WAITING_FOR_A_PLANE_TO_LAND;
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
	public StatesPorter getState() {
		return state;
	}
	public void setState(StatesPorter state) {
		this.state = state;
	}
	
}
