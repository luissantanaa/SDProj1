package airport;

import Interfaces.ArrivalTransferTermBDriverInterface;
import Interfaces.DepartureTransTermBDriverInterface;
import monitors.DepartureTransTerm;
import states.StatesBusD;

public class BusDriver extends Thread{
	private StatesBusD state;
	volatile boolean end = false;
	private DepartureTransTermBDriverInterface departuretransmonitor;
	private ArrivalTransferTermBDriverInterface arrivaltranferterm;
	private Logger logger;
	
	
	public BusDriver(DepartureTransTermBDriverInterface departuretransmonitor,
						ArrivalTransferTermBDriverInterface arrivaltranferterm,
						Logger logger) {
		super();
		this.departuretransmonitor = departuretransmonitor;
		this.state =  StatesBusD.PARKING_AT_THE_ARRIVAL_TERMINAL;
		this.arrivaltranferterm = arrivaltranferterm;
		this.logger = logger;
	}
	public StatesBusD getStates() {
		return state;
	}
	public void setState(StatesBusD state) {
		this.state = state;
	}
	
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
	
		s = s + " --- " + "" + " " + "" + " "; 
		//System.out.println("\n\n\n " + s + "\n\n\n");
		return s;
	}
	
	public void run() {
		while(!end) {
			switch(this.state) {
				case PARKING_AT_THE_ARRIVAL_TERMINAL:
					logger.toPrint();
					//System.out.println(this.state);
					if(!arrivaltranferterm.hasDaysWorkEnded()) {
						//System.out.println("\n\n\nif1 " + this.state);
						if(!arrivaltranferterm.BusNotFull()) {
							//System.out.println("\n\n\nif2 " + this.state);
							arrivaltranferterm.announcingBusBoarding();
							this.goToDepartureTerminal();
						}
					}else {
						//System.out.println("\n\n\nelse " + this.state);
						endThread();
					}
					
					break;
				case DRIVING_FORWARD:
					logger.toPrint();
					//System.out.println("\n\n\n" +this.state);
					this.parkTheBusAndLetPassOff();
					break;
				
				case PARKING_AT_THE_DEPARTURE_TERMINAL:
					logger.toPrint();
					//System.out.println("\n\n\nif1 " + this.state);
					this.departuretransmonitor.arriveDepTransTerm();
					//System.out.println("\n\n\nif2 " + this.state);
					this.departuretransmonitor.waitForPassengers();
					//System.out.println("\n\n\nif3 " + this.state);
					this.goToArrivalTerminal();	
					//System.out.println("\n\n\nif4 " + this.state);
					break;
									
				case DRIVING_BACKWARD:
					logger.toPrint();
					//System.out.println("\n\n\nif1 " + this.state);
					this.parkTheBus();
					//System.out.println("\n\n\nif2 " + this.state);
					break;
					
				default:
			}
		}

	}

	public void endThread(){
        end = true;
    }

	public void goToArrivalTerminal() {
		this.state = StatesBusD.DRIVING_BACKWARD;
	}
	public void parkTheBus() {
		this.state = StatesBusD.PARKING_AT_THE_ARRIVAL_TERMINAL;
		arrivaltranferterm.announcingBusBoarding();
	}
	public void goToDepartureTerminal() {
		this.state = StatesBusD.DRIVING_FORWARD;
	}
	public void parkTheBusAndLetPassOff() {
		this.state = StatesBusD.PARKING_AT_THE_DEPARTURE_TERMINAL;
	}
}
