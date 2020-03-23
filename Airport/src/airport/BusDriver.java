package airport;

import Interfaces.ArrivalLoungeInterfaceBDriver;
import Interfaces.ArrivalTransferTermBDriverInterface;
import Interfaces.DepartureTransTermBDriverInterface;
import states.StatesBusD;

public class BusDriver extends Thread{
	private StatesBusD state;
	volatile boolean end = false;
	private DepartureTransTermBDriverInterface departuretransmonitor;
	private ArrivalTransferTermBDriverInterface arrivaltranferterm;
	private ArrivalLoungeInterfaceBDriver arrivallounge;
	private Logger logger;
	
	
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
		return s;
	}
	
	public void run() {
		while(!end) {
			switch(this.state) {
				case PARKING_AT_THE_ARRIVAL_TERMINAL:
					logger.toPrint();
					if(!arrivaltranferterm.hasDaysWorkEnded()) {
						int numPass = arrivallounge.goCollectPassengers();
						if(numPass!=-1) {
							arrivaltranferterm.announcingBusBoarding(numPass);
							if(!arrivaltranferterm.BusNotFull()) {
								this.goToDepartureTerminal();
							}
						}
					}else {
						endThread();
					}
					
					break;
				case DRIVING_FORWARD:
					logger.toPrint();
					this.parkTheBusAndLetPassOff();
					break;
				
				case PARKING_AT_THE_DEPARTURE_TERMINAL:
					logger.toPrint();
					this.departuretransmonitor.arriveDepTransTerm();
					this.departuretransmonitor.waitForPassengers();
					this.goToArrivalTerminal();	
					break;
									
				case DRIVING_BACKWARD:
					logger.toPrint();
					this.parkTheBus();
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
	}
	public void goToDepartureTerminal() {
		this.state = StatesBusD.DRIVING_FORWARD;
	}
	public void parkTheBusAndLetPassOff() {
		this.state = StatesBusD.PARKING_AT_THE_DEPARTURE_TERMINAL;
	}
}
