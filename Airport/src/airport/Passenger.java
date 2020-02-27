package airport;

import java.util.List;
import states.StatesPerson;

public class Passenger {
	
	StatesPerson state;
	int id;
	List<Bag> b;
	boolean dest;
	String time = null;
	public Passenger(int id, List<Bag> b, boolean destination) {
		this.id = id;
		this.b = b;
		this.dest = destination;
		this.state = StatesPerson.AT_THE_DISEMBARKING_ZONE;
	}
	public Passenger(int id, List<Bag> b, boolean destination, String time) {
		this.id = id;
		this.b = b;
		this.dest = destination;
		this.time = time;
		this.state = StatesPerson.AT_THE_DISEMBARKING_ZONE;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean HasBag() {
		if(b.size()==0) {
			return false;
		}
		return true;
	}
	public List<Bag> getB() {
		return b;
	}
	public void setB(List<Bag> b) {
		this.b = b;
	}
	public boolean isDest() {
		return dest;
	}
	public void setDest(boolean dest) {
		this.dest = dest;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public StatesPerson getState() {
		return state;
	}
	public void setState(StatesPerson state) {
		this.state = state;
	}
}
