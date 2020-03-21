package airport;

import java.util.List;

import Interfaces.ArrivalLoungeInterfacePassenger;
import Interfaces.ArrivalTransferTermPassengerInterface;
import Interfaces.BaggageCollectPointPassengerInterface;
import Interfaces.BaggageReclaimOfficePassengerInterface;
import Interfaces.DepartureTransTermPassengerInterface;
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
	private DepartureTransTermPassengerInterface departurearrivaltermpassengerinterface;
	private DepartureTermEntrancePassengerInterface departuretermentrancepassengerinterface;
	
	
	// Variaveis Pessoais
	private int id;
	private List<Bag> b;
	private boolean dest;
	private String time = null;
	private int bagCollected = 0;
	private String destino;
	private Logger logger;
	
	public Passenger(Logger logger,int id, List<Bag> b, 
			         boolean destination, 
			         ArrivalLoungeInterfacePassenger arrivalmonitor, 
			         BaggageCollectPointPassengerInterface baggagecollectpoint,
			         ArrivalTransferTermPassengerInterface arrivaltransfertermPassengerinterface,
			         BaggageReclaimOfficePassengerInterface baggageReclaimofficepassengerinterface,
			         DepartureTransTermPassengerInterface departurearrivaltermpassengerinterface,
			         DepartureTermEntrancePassengerInterface departuretermentrancepassengerinterface) {
		
		this.logger = logger;
		this.id = id;
		this.b = b;
		this.dest = destination;
		this.state = StatesPerson.AT_THE_DISEMBARKING_ZONE;
		
		//Monitors interfaces
		this.arrivalmonitor=arrivalmonitor;
		this.baggagecollectpoint=baggagecollectpoint;
		this.arrivaltransfertermPassengerinterface = arrivaltransfertermPassengerinterface;
		this.baggageReclaimofficepassengerinterface=baggageReclaimofficepassengerinterface;
		this.departurearrivaltermpassengerinterface=departurearrivaltermpassengerinterface;
		this.departuretermentrancepassengerinterface=departuretermentrancepassengerinterface;
		
		if(dest){
			destino = "FDT";
		}
		
	}
	
	public Passenger(int id, List<Bag> b, boolean destination, String time, 
			         ArrivalLoungeInterfacePassenger arrivalmonitor, 
			         BaggageCollectPointPassengerInterface baggagecollectpoint,
			         ArrivalTransferTermPassengerInterface arrivaltransfertermPassengerinterface,
			         BaggageReclaimOfficePassengerInterface baggageReclaimofficepassengerinterface,
			         DepartureTransTermPassengerInterface departurearrivaltermpassengerinterface,
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
	
	public String getString() {
		String s="";
		switch(this.state) {
			case AT_THE_DISEMBARKING_ZONE:
				s = s + "WSD";
				break;
			case AT_THE_LUGGAGE_COLLECTION_POINT:
				s = s + "LCP";
				break;
				
			case AT_THE_ARRIVAL_TRANSFER_TERMINAL:
				s = s + "ATT";
				break;
			case AT_THE_BAGGAGE_RECLAIM_OFFICE:
				s = s + "BRO";
				break;
			case TERMINAL_TRANSFER:
				s = s + "TRT";
				break;
				
			case AT_THE_DEPARTURE_TRANSFER_TERMINAL:
				s = s + "DTT";
				break;
			default:
				s = s + "EAT";	
		}
		
		if(this.dest) {
			s = s + " FDT " + this.b.size() + " " + this.bagCollected + " "; 
		}else {
			s = s + " TRT " + this.b.size() + " " + this.bagCollected + " "; 
		}
		
		return s;
	}
	// Maquina de Estados
	public void run() {
		logger.addPassengers(this);
		logger.toPrint();
		while(!stateIsFinal()) {
			switch(this.state) {
				case AT_THE_DISEMBARKING_ZONE:
					
					// Retorna o estado dependendo se tem malas e se chegou ao seu destino
					this.state=arrivalmonitor.whatShouldIDo(this.b, this.dest);
					logger.toPrint();
					break;
					
				case AT_THE_LUGGAGE_COLLECTION_POINT:
					logger.toPrint();
					for(int i=0;i< b.size();i++) {
						boolean bagFound = baggagecollectpoint.collectBag(this.b.get(i));
						if(bagFound) {
							this.bagCollected++;
							logger.decrementCB();
							logger.toPrint();
						}else {
							logger.incrementLost();
						}
					}
					
					if(this.bagCollected==b.size()) { // verifica se � possivel recolher a mala
						
						goHome(); // vai para casa
					}else {
						reportMissingBag(); // reportar mala perdida
					}
					break;
					
				case AT_THE_ARRIVAL_TRANSFER_TERMINAL:
					
					if(arrivaltransfertermPassengerinterface.enterTheBus(this)) { // verifica se � possivel entrar no autocarro
						enterTheBus(); // entrar no autocarro
					}
					break;
				case AT_THE_BAGGAGE_RECLAIM_OFFICE:
					//System.out.println("state,id,bags  " + this.state +" : "+ this.id + " : " + this.b.size());
					baggageReclaimofficepassengerinterface.reportMissingBag(this); // reportar mala perdida
					goHome(); // ir para casa
					
					break;
				case TERMINAL_TRANSFER:
					//System.out.println("state,id,bags  " + this.state +" : "+ this.id + " : " + this.b.size());

					if(departurearrivaltermpassengerinterface.leaveTheBus(this)) { // sair do autocarro
						
						leaveTheBus();
					}
				
					break;
					
				case AT_THE_DEPARTURE_TRANSFER_TERMINAL:
					//System.out.println("state,id,bags  " + this.state +" : "+ this.id + " : " + this.b.size());
					//departuretermentrancepassengerinterface.prepareNextLeg(this); // preparar proximo voo
					prepareNextLeg();
					break;
				default:
			}
		}
	}
	
	
	// Mudan�as de estado
	public void goHome() {
		this.state = StatesPerson.EXITING_THE_ARRIVAL_TERMINAL;
		logger.toPrint();
	}
	public void goCollectABag() {
		this.state= StatesPerson.AT_THE_LUGGAGE_COLLECTION_POINT;
		logger.toPrint();
	}
	public void takeABus() {
		 this.state= StatesPerson.AT_THE_ARRIVAL_TRANSFER_TERMINAL;
		 logger.toPrint();
	 }
	public void reportMissingBag() {
		 this.state= StatesPerson.AT_THE_BAGGAGE_RECLAIM_OFFICE;
		 logger.toPrint();
	 }
	public void enterTheBus() {
		 this.state= StatesPerson.TERMINAL_TRANSFER;
		 logger.toPrint();
	 }
	public void leaveTheBus() {
		 this.state= StatesPerson.AT_THE_DEPARTURE_TRANSFER_TERMINAL;
		 logger.toPrint();
	 }
	public void prepareNextLeg() {
		 this.state= StatesPerson.ENTERING_THE_DEPARTURE_TERMINAL ;
		 logger.toPrint();
	 } 	
}
