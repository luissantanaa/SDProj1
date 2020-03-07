package airport;

import java.util.List;

import Interfaces.ArrivalLoungeInterfacePassenger;
import Interfaces.ArrivalTransferTermPassengerInterface;
import Interfaces.BaggageCollectPointPassengerInterface;
import Interfaces.BaggageReclaimOfficePassengerInterface;
import Interfaces.DepartureArrivalTermPassengerInterface;
import Interfaces.DepartureTermEntrancePassengerInterface;
import states.StatesPerson;
import monitors.ArrivalLounge;
import monitors.BaggageCollectPoint;
public class Passenger {
	
	private StatesPerson state;
	
	private ArrivalLoungeInterfacePassenger arrivalmonitor;
	private BaggageCollectPointPassengerInterface baggagecollectpoint;
	private ArrivalTransferTermPassengerInterface arrivaltransfertermPassengerinterface;
	private BaggageReclaimOfficePassengerInterface baggageReclaimofficepassengerinterface;
	private DepartureArrivalTermPassengerInterface departurearrivaltermpassengerinterface;
	private DepartureTermEntrancePassengerInterface departuretermentrancepassengerinterface;
	
	private int id;
	private List<Bag> b;
	private boolean dest;
	private String time = null;
	
	public Passenger(int id, List<Bag> b, 
			         boolean destination, 
			         ArrivalLoungeInterfacePassenger arrivalmonitor, 
			         BaggageCollectPointPassengerInterface baggagecollectpoint,
			         ArrivalTransferTermPassengerInterface arrivaltransfertermPassengerinterface,
			         BaggageReclaimOfficePassengerInterface baggageReclaimofficepassengerinterface,
			         DepartureArrivalTermPassengerInterface departurearrivaltermpassengerinterface,
			         DepartureTermEntrancePassengerInterface departuretermentrancepassengerinterface) {
		
		this.id = id;
		this.b = b;
		this.dest = destination;
		this.arrivalmonitor=arrivalmonitor;
		this.state = StatesPerson.AT_THE_DISEMBARKING_ZONE;
		this.baggagecollectpoint=baggagecollectpoint;
		this.baggageReclaimofficepassengerinterface=baggageReclaimofficepassengerinterface;
		this.departurearrivaltermpassengerinterface=departurearrivaltermpassengerinterface;
		this.departuretermentrancepassengerinterface=departuretermentrancepassengerinterface;
		
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
	public boolean getDest() {
		return this.dest;
	}
	
	
	
	
	public boolean stateIsFinal() {
		if(this.state== StatesPerson.EXITING_THE_ARRIVAL_TERMINAL || this.state== StatesPerson.ENTERING_THE_DEPARTURE_TERMINAL) {
			return true;
		}
		return false;
	}
	
	
	
	
	
	
	
	public void run() {
		while(!stateIsFinal()) {
			switch(this.state) {
				case AT_THE_DISEMBARKING_ZONE:
					arrivalmonitor.whatShouldIDo(this);
					break;
				case AT_THE_LUGGAGE_COLLECTION_POINT:
					baggagecollectpoint.collectBag(this);
					break;
				case AT_THE_ARRIVAL_TRANSFER_TERMINAL:
					arrivaltransfertermPassengerinterface.enterTheBus(this);
					break;
				case AT_THE_BAGGAGE_RECLAIM_OFFICE:
					baggageReclaimofficepassengerinterface.reportMissingBag(this);
					break;
				case TERMINAL_TRANSFER:
					departurearrivaltermpassengerinterface.leaveTheBus(this);
					break;
				case AT_THE_DEPARTURE_TRANSFER_TERMINAL:
					departuretermentrancepassengerinterface.prepareNextLeg(this);
					break;
				default:
			}
		}
	}
	
	
	
	// Mudanças de estado
	public void goHome() {
		this.state = StatesPerson.EXITING_THE_ARRIVAL_TERMINAL;
	}
	public void goCollectABag() {
		this.state= StatesPerson.AT_THE_LUGGAGE_COLLECTION_POINT;
	}
	 public void takeABus() {
		 this.state= StatesPerson.AT_THE_ARRIVAL_TRANSFER_TERMINAL;
	 }
	 
	 public void reportMissingBag() {
		 this.state= StatesPerson.AT_THE_BAGGAGE_RECLAIM_OFFICE;
	 }
	 
	 public void enterTheBus() {
		 this.state= StatesPerson.TERMINAL_TRANSFER;
	 }
	 
	 public void leaveTheBus() {
		 this.state= StatesPerson.AT_THE_DEPARTURE_TRANSFER_TERMINAL;
	 }

	 public void prepareNextLeg() {
		 this.state= StatesPerson.ENTERING_THE_DEPARTURE_TERMINAL ;
	 } 	
}
