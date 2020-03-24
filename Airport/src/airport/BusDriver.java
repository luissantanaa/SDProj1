package airport;

import Interfaces.ArrivalLoungeInterfaceBDriver;
import Interfaces.ArrivalTransferTermBDriverInterface;
import Interfaces.DepartureTransTermBDriverInterface;
import states.StatesBusD;

public class BusDriver extends Thread{
	
	private StatesBusD state;
	//boolean volatil para sinalizar a morte da thread
	volatile boolean end = false;
	//monitores usados pela thread
	private DepartureTransTermBDriverInterface departuretransmonitor;
	private ArrivalTransferTermBDriverInterface arrivaltranferterm;
	private ArrivalLoungeInterfaceBDriver arrivallounge;
	
	private Logger logger;
	
	//construtor
	public BusDriver(DepartureTransTermBDriverInterface departuretransmonitor,
						ArrivalTransferTermBDriverInterface arrivaltranferterm,
						ArrivalLoungeInterfaceBDriver arrivallounge,
						Logger logger) {
		super();
		this.departuretransmonitor = departuretransmonitor;
		this.state =  StatesBusD.PARKING_AT_THE_ARRIVAL_TERMINAL;
		this.arrivaltranferterm = arrivaltranferterm;
		this.logger = logger;
		this.arrivallounge=arrivallounge;
	}
	
	//getters and setters
	public StatesBusD getStates() {
		return state;
	}
	public void setState(StatesBusD state) {
		this.state = state;
	}
	
	//função usada pelo logger
	public String getString() {
		String s="";
	switch(this.state) {
		case PARKING_AT_THE_ARRIVAL_TERMINAL:
			s = s + "PKAT";
			break;
		case DRIVING_FORWARD:
			s = s + "DRFW";
			break;
		case PARKING_AT_THE_DEPARTURE_TERMINAL:
			s = s + "PKDT";
			break;
		case DRIVING_BACKWARD:
			s = s + "DRBW";
			break;
		default:
		}
		return s;
	}
	
	
	//função que simula o lifecycle da thread
	public void run() {
		while(!end) {
			switch(this.state) {
				case PARKING_AT_THE_ARRIVAL_TERMINAL:
					logger.toPrint();
					if(!arrivaltranferterm.hasDaysWorkEnded()) { //enquanto o dia de trabalho nao acabar verifica se ha passageiros para recolher
						int numPass = arrivallounge.goCollectPassengers();
						if(numPass!=-1) { //se existem passageiros para recolher, anuncia que esta a espera de passageiros
							arrivaltranferterm.announcingBusBoarding(numPass);
							if(!arrivaltranferterm.BusNotFull()) { //se o autocarro esta cheio parte
								this.goToDepartureTerminal();
							}
						}
					}else {
						endThread(); //sinaliza a morte da thread
					}
					
					break;
				case DRIVING_FORWARD:
					logger.toPrint(); //estado de transição
					this.parkTheBusAndLetPassOff();
					break;
				
				case PARKING_AT_THE_DEPARTURE_TERMINAL:
					logger.toPrint();
					this.departuretransmonitor.arriveDepTransTerm();
					this.departuretransmonitor.waitForPassengers(); //esperar por passageiros
					this.goToArrivalTerminal();	//mudança de estado
					break;
									
				case DRIVING_BACKWARD:
					logger.toPrint();//estado de transição
					this.parkTheBus();
					break;
			
				default:
			}
		}

	}

	public void endThread(){
        end = true;
    }
	
	
	//funções de transição de estados
	public void goToArrivalTerminal() {
		this.state = StatesBusD.DRIVING_BACKWARD;
	}
	public void parkTheBus() {
		this.state = StatesBusD.PARKING_AT_THE_ARRIVAL_TERMINAL;
	}
	public void goToDepartureTerminal() {
		this.state = StatesBusD.DRIVING_FORWARD;
	}
	public void parkTheBusAndLetPassOff() {
		this.state = StatesBusD.PARKING_AT_THE_DEPARTURE_TERMINAL;
	}
}
