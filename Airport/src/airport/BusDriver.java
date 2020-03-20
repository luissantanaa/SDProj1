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
	
	public void run() {
		while(!end) {
			switch(this.state) {
				case PARKING_AT_THE_ARRIVAL_TERMINAL:
					//faltam cenas
					if(!arrivaltranferterm.hasDaysWorkEnded()) {
						if(!arrivaltranferterm.BusNotFull()) {
							
							this.goToDepartureTerminal();
						}
					}else {
						endThread();
					}
					
					break;
				case DRIVING_FORWARD:
				
					this.parkTheBusAndLetPassOff();
					break;
				
				case PARKING_AT_THE_DEPARTURE_TERMINAL:
					this.departuretransmonitor.arriveDepTransTerm();
					this.departuretransmonitor.waitForPassengers();
					this.goToArrivalTerminal();	
					
					break;
									
				case DRIVING_BACKWARD:
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
		arrivaltranferterm.announcingBusBoarding();
	}
	public void goToDepartureTerminal() {
		this.state = StatesBusD.DRIVING_FORWARD;
	}
	public void parkTheBusAndLetPassOff() {
		this.state = StatesBusD.PARKING_AT_THE_DEPARTURE_TERMINAL;
	}
}
