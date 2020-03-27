package airport;

import java.util.List;


import Interfaces.ArrivalLoungeInterfacePassenger;
import Interfaces.ArrivalTermExitPassengerInterface;
import Interfaces.ArrivalTransferTermPassengerInterface;
import Interfaces.BaggageCollectPointPassengerInterface;
import Interfaces.BaggageReclaimOfficePassengerInterface;
import Interfaces.DepartureTransTermPassengerInterface;
import Interfaces.DepartureTermEntrancePassengerInterface;
import states.StatesPerson;


/**
 * 
 * classe que implementa os passageiros usados na simulação
 * função run simula o lifecycle da thread
 *
 */
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
	private ArrivalTermExitPassengerInterface arrivaltermexitpassengerinterface;
	
	// Variaveis Pessoais
	private int id;
	private List<Bag> b;
	private boolean dest;
	private String time = null;
	private int bagCollected = 0;
	private Logger logger;
	
	
	private volatile boolean end=false; 
	
	
	/**
	 * Construtor da classe
	 * @param logger - usado para logs durante a simulação
	 * @param id - identificador do passageiro
	 * @param b - lista de malas do passageiro
	 * @param destination - boolean que indica se o passageiro esta no destino ou nao
	 * @param arrivalmonitor - Monitor Arrival Lounge
	 * @param baggagecollectpoint - Monitor Baggage Collection Point
	 * @param arrivaltransfertermPassengerinterface - Monitor Arrival Transfer Terminal
	 * @param baggageReclaimofficepassengerinterface - Monitor Baggage Reclaim Office
	 * @param departurearrivaltermpassengerinterface - Monitor Departure Arrival Terminal
	 * @param departuretermentrancepassengerinterface - Monitor Departure Terminal Entrance
	 * @param arrivaltermexitpassengerinterface - Monitor Arrival Terminal Exit
	 */
	//construtor para passageiros que apenas realizam um voo
	public Passenger(Logger logger,int id, List<Bag> b, 
			         boolean destination, 
			         ArrivalLoungeInterfacePassenger arrivalmonitor, 
			         BaggageCollectPointPassengerInterface baggagecollectpoint,
			         ArrivalTransferTermPassengerInterface arrivaltransfertermPassengerinterface,
			         BaggageReclaimOfficePassengerInterface baggageReclaimofficepassengerinterface,
			         DepartureTransTermPassengerInterface departurearrivaltermpassengerinterface,
			         DepartureTermEntrancePassengerInterface departuretermentrancepassengerinterface,
			         ArrivalTermExitPassengerInterface arrivaltermexitpassengerinterface) {
		
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
		this.arrivaltermexitpassengerinterface=arrivaltermexitpassengerinterface;

		
	}	
	
	/**
	 * 
	 * Getters and setters da classe
	 */
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
	
	
	/**
	 * 
	 * Função usada pelo logger que passa os estados para a sua respetica sigla
	 */
	//função usada pelo logger para passar os estados para a respetiva sigla
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
			case EXITING_THE_ARRIVAL_TERMINAL:
				s = s + "EAT";	
				break;
			case ENTERING_THE_DEPARTURE_TERMINAL:
				s = s + "EDT";
				break;
		}
		
		if(this.dest) {
			s = s + " FDT " + this.b.size() + " " + this.bagCollected + " "; 
		}else {
			s = s + " TRT " + this.b.size() + " " + this.bagCollected + " "; 
		}
		
		return s;
	}
	
	/**
	 * função run simula o lifecycle da thread
	 */
	// Maquina de Estados
	public void run() {
		logger.addPassengers(this);
		logger.toPrint();
		while(!end) {
			switch(this.state) {
				case AT_THE_DISEMBARKING_ZONE:
					// Retorna o estado dependendo se tem malas e se chegou ao seu destino
					this.state=arrivalmonitor.whatShouldIDo(this.b, this.dest);
					logger.toPrint();
					break;
					
				case AT_THE_LUGGAGE_COLLECTION_POINT:
					logger.toPrint();
					for(int i=0;i< b.size();i++) {	//ve se é possivel recolher a/as mala/as
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
						goExitArrivalTerm(); // vai para casa
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
					goExitArrivalTerm(); // ir para casa
					
					break;
				case TERMINAL_TRANSFER:
					if(departurearrivaltermpassengerinterface.leaveTheBus(this)) { // sair do autocarro
						leaveTheBus();
					}
				
					break;
					
				case EXITING_THE_ARRIVAL_TERMINAL:	//estado final, termina a thread
					if(arrivaltermexitpassengerinterface.GoHome(this)) {
						endThread();
						
						break;
					}
					break;
				case AT_THE_DEPARTURE_TRANSFER_TERMINAL:
					if(departuretermentrancepassengerinterface.prepareNextLeg(this)) {
						// preparar proximo voo
						 prepareNextLeg();
						 
					}
					break;
				case ENTERING_THE_DEPARTURE_TERMINAL:
					endThread();
					break;
				default:
			}

		}
		
	
	}
	
	
	// Mudan�as de estado
	/**
	 * função transição de estado
	 */
	public void goExitArrivalTerm() {
		this.state = StatesPerson.EXITING_THE_ARRIVAL_TERMINAL;
	
		logger.toPrint();
	}
	
	/**
	 * função transição de estado
	 */
	public void goCollectABag() {
		this.state= StatesPerson.AT_THE_LUGGAGE_COLLECTION_POINT;

		logger.toPrint();
	}
	
	/**
	 * função transição de estado
	 */
	public void takeABus() {
		 this.state= StatesPerson.AT_THE_ARRIVAL_TRANSFER_TERMINAL;
		 logger.toPrint();
	 }
	
	/**
	 * função transição de estado
	 */
	public void reportMissingBag() {
		 this.state= StatesPerson.AT_THE_BAGGAGE_RECLAIM_OFFICE;
		 logger.toPrint();
	 }
	
	/**
	 * função transição de estado
	 */
	public void enterTheBus() {
		 this.state= StatesPerson.TERMINAL_TRANSFER;
		 logger.toPrint();
	 }
	
	/**
	 * função transição de estado
	 */
	public void leaveTheBus() {
		 this.state= StatesPerson.AT_THE_DEPARTURE_TRANSFER_TERMINAL;
		 logger.toPrint();
	 }
	
	/**
	 * função transição de estado
	 */
	public void prepareNextLeg() {
		 this.state= StatesPerson.ENTERING_THE_DEPARTURE_TERMINAL ;
		
		 logger.toPrint();
	 } 	
	
	/**
	 * função para terminar a thread
	 */
	public void endThread() {
		end = true;
		//logger.toPrint();
	}
}
