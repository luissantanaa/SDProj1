package airport;


import Interfaces.ArrivalLoungeInterfacePorter;
import Interfaces.BaggageCollectPointPorterInterface;
import Interfaces.TempStorageInterfacePorter;
import states.StatesPorter;

public class Porter extends Thread{
	private ArrivalLoungeInterfacePorter arrivalmonitor;
	private BaggageCollectPointPorterInterface baggageCollectPointMonitor;
	private Bag b;
	private StatesPorter state;
	private TempStorageInterfacePorter tempstoragemonitor;
	private Logger logger ;
	volatile boolean end = false;


	public Porter( Logger logger , ArrivalLoungeInterfacePorter arrivalmonitor, BaggageCollectPointPorterInterface baggageCollectPointMonitor, TempStorageInterfacePorter tempstoragemonitor) {
		super();
		this.arrivalmonitor = arrivalmonitor;
		this.baggageCollectPointMonitor = baggageCollectPointMonitor;
		this.tempstoragemonitor = tempstoragemonitor;
		this.state = StatesPorter.WAITING_FOR_A_PLANE_TO_LAND;
		this.logger = logger;
	}
	public Bag getB() {
		return b;
	}
	public void setB(Bag b) {
		this.b = b;
	}
	public StatesPorter getStates() {
		return state;
	}
	public String getString() {
		String s="";
		switch(this.state) {
		case WAITING_FOR_A_PLANE_TO_LAND:
			s = s + "WPTL";
			break;
		case AT_THE_PLANES_HOLD:
			s = s + "APLH";
			break;
		case AT_THE_LUGGAGE_BELT_CONVEYOR:
			s = s + "ALCB";
			break;
		case AT_THE_STOREROOM:
			s = s + "ALCR";
			break;
		default:
				
		}
		return s;

	}
	
	
	public void run() {
		while(!end){
			switch(this.state) {
				case WAITING_FOR_A_PLANE_TO_LAND:
					if(arrivalmonitor.takeARest()){
						logger.toPrint();
						this.tryToCollectABag();
					}
					break;
				case AT_THE_PLANES_HOLD:
			
					this.b = this.arrivalmonitor.collectBag();
					if(this.b == null) {
						
						this.noMoreBagsToCollect();
					}else {
						if(this.b.isDest()) {
							this.carryItToAppropriateStore(StatesPorter.AT_THE_LUGGAGE_BELT_CONVEYOR);
							this.baggageCollectPointMonitor.addBag(this.b);
							logger.incrementCB();
							logger.toPrint();
							
						}else {
							this.carryItToAppropriateStore(StatesPorter.AT_THE_STOREROOM);
							logger.toPrint();
							this.tempstoragemonitor.addBag(this.b);
							
						}
						
					}
				
					break;
				case AT_THE_LUGGAGE_BELT_CONVEYOR:
					this.tryToCollectABag();
					break;
				case AT_THE_STOREROOM:
					this.tryToCollectABag();
					break;
				default:
			}
		}
	}
	
	public void tryToCollectABag() {
		this.state = StatesPorter.AT_THE_PLANES_HOLD;
		logger.toPrint();
	}
	
	public void carryItToAppropriateStore(StatesPorter state) {
		this.state = state;
	}

	public void endThread(){
        end = true;
    }
	
	public void noMoreBagsToCollect() {
		this.state = StatesPorter.WAITING_FOR_A_PLANE_TO_LAND;
	}
	
}
