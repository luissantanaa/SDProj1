package airport;

import java.util.List;

import Interfaces.ArrivalLoungeInterfacePassenger;
import Interfaces.ArrivalTransferTermPassengerInterface;
import Interfaces.BaggageCollectPointPassengerInterface;
import Interfaces.BaggageReclaimOfficePassengerInterface;
import Interfaces.DepartureArrivalTermPassengerInterface;
import Interfaces.DepartureTermEntrancePassengerInterface;
import states.StatesPerson;
public class Passenger extends Thread{
	
	
	// Estado em que se encontra
	private StatesPerson state;
	
	
	// Interfaces para os monitors
	private ArrivalLoungeInterfacePassenger arrivalmonitor;
	private BaggageCollectPointPassengerInterface baggagecollectpoint;
	private ArrivalTransferTermPassengerInterface arrivaltransfertermPassengerinterface;
	private BaggageReclaimOfficePassengerInterface baggageReclaimofficepassengerinterface;
	private DepartureArrivalTermPassengerInterface departurearrivaltermpassengerinterface;
	private DepartureTermEntrancePassengerInterface departuretermentrancepassengerinterface;
	
	
	// Variaveis Pessoais
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
		this.state = StatesPerson.AT_THE_DISEMBARKING_ZONE;
		
		//Monitors interfaces
		this.arrivalmonitor=arrivalmonitor;
		this.baggagecollectpoint=baggagecollectpoint;
		this.baggageReclaimofficepassengerinterface=baggageReclaimofficepassengerinterface;
		this.departurearrivaltermpassengerinterface=departurearrivaltermpassengerinterface;
		this.departuretermentrancepassengerinterface=departuretermentrancepassengerinterface;
		
	}
	
	public Passenger(int id, List<Bag> b, boolean destination, String time, 
			         ArrivalLoungeInterfacePassenger arrivalmonitor, 
			         BaggageCollectPointPassengerInterface baggagecollectpoint,
			         ArrivalTransferTermPassengerInterface arrivaltransfertermPassengerinterface,
			         BaggageReclaimOfficePassengerInterface baggageReclaimofficepassengerinterface,
			         DepartureArrivalTermPassengerInterface departurearrivaltermpassengerinterface,
			         DepartureTermEntrancePassengerInterface departuretermentrancepassengerinterface) {
		
		this.id = id;
		this.b = b;
		this.dest = destination;
		this.time = time;
		this.state = StatesPerson.AT_THE_DISEMBARKING_ZONE;
		
		//Monitors interfaces
		this.arrivalmonitor=arrivalmonitor;
		this.baggagecollectpoint=baggagecollectpoint;
		this.baggageReclaimofficepassengerinterface=baggageReclaimofficepassengerinterface;
		this.departurearrivaltermpassengerinterface=departurearrivaltermpassengerinterface;
		this.departuretermentrancepassengerinterface=departuretermentrancepassengerinterface;

	}
	
	
	
	
	//Getters and Setters
	public int getPId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public StatesPerson getStates() {
		return state;
	}
	public void setState(StatesPerson state) {
		this.state = state;
	}
	public boolean getDest() {
		return this.dest;
	}
	
	
	// Verifica se o passageiro se encontra num estado final
	public boolean stateIsFinal() {
		if(this.state== StatesPerson.EXITING_THE_ARRIVAL_TERMINAL || this.state== StatesPerson.ENTERING_THE_DEPARTURE_TERMINAL) {
			return true;
		}
		return false;
	}
	
	
	
	// Maquina de Estados
	public void run() {
		while(!stateIsFinal()) {
			switch(this.state) {
				case AT_THE_DISEMBARKING_ZONE:
					
					// Retorna o estado dependendo se tem malas e se chegou ao seu destino
					this.state=arrivalmonitor.whatShouldIDo(this.b, this.dest);
					break;
					
				case AT_THE_LUGGAGE_COLLECTION_POINT:
					
						if(baggagecollectpoint.collectBag(this.b)) { // verifica se � possivel recolher a mala
							goHome(); // vai para casa
						}else {
							reportMissingBag(); // reportar mala perdida
						}
					break;
					
				case AT_THE_ARRIVAL_TRANSFER_TERMINAL:
					if(arrivaltransfertermPassengerinterface.enterTheBus(this)) { // verifica se � possivel entrar no autocarro
						takeABus(); // entrar no autocarro
					}
					break;
				case AT_THE_BAGGAGE_RECLAIM_OFFICE:
					
					baggageReclaimofficepassengerinterface.reportMissingBag(this); // reportar mala perdida
					goHome(); // ir para casa
					
					break;
				case TERMINAL_TRANSFER:
					departurearrivaltermpassengerinterface.leaveTheBus(this); // sair do autocarro
					leaveTheBus();
					break;
					
				case AT_THE_DEPARTURE_TRANSFER_TERMINAL:
					departuretermentrancepassengerinterface.prepareNextLeg(this); // preparar proximo voo
					prepareNextLeg();
					break;
				default:
			}
		}
	}
	
	
	// Mudan�as de estado
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
