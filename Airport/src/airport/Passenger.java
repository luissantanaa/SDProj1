package airport;

public class Passenger {
	enum states {AT_THE_DISEMBARKING_ZONE, AT_THE_LUGGAGE_COLLECTION_POINT, AT_THE_BAGGAGE_RECLAIM_OFFICE,
		EXITING_THE_ARRIVAL_TERMINAL, AT_THE_ARRIVAL_TRANSFER_TERMINAL, TERMINAL_TRANSFER,
		AT_THE_DEPARTURE_TRANSFER_TERMINAL, ENTERING_THE_DEPARTURE_TERMINAL};
	states state;
	int id;
	Bag b;
	boolean dest;
	String time = null;
	public Passenger(int id, Bag b, boolean destination) {
		this.id = id;
		this.b = b;
		this.dest = destination;
		this.state = states.AT_THE_DISEMBARKING_ZONE;
	}
	public Passenger(int id, Bag b, boolean destination, String time) {
		this.id = id;
		this.b = b;
		this.dest = destination;
		this.time = time;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Bag getB() {
		return b;
	}
	public void setB(Bag b) {
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
	public states getState() {
		return state;
	}
	public void setState(states state) {
		this.state = state;
	}
}
