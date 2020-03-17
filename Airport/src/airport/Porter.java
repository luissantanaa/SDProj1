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

	volatile boolean end = false;


	public Porter( ArrivalLoungeInterfacePorter arrivalmonitor, BaggageCollectPointPorterInterface baggageCollectPointMonitor, TempStorageInterfacePorter tempstoragemonitor) {
		super();
		this.arrivalmonitor = arrivalmonitor;
		this.baggageCollectPointMonitor = baggageCollectPointMonitor;
		this.tempstoragemonitor = tempstoragemonitor;
		this.state = StatesPorter.WAITING_FOR_A_PLANE_TO_LAND;
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
						this.tryToCollectABag();
					}
					break;
				case AT_THE_PLANES_HOLD:
			
					this.b = this.arrivalmonitor.collectBag();
					if(this.b == null) {
						
						this.noMoreBagsToCollect();
					}else {
						if(this.b.isDest()) {
							this.baggageCollectPointMonitor.addBag(this.b);
							this.carryItToAppropriateStore(StatesPorter.AT_THE_LUGGAGE_BELT_CONVEYOR);
						}else {
							this.tempstoragemonitor.addBag(this.b);
							this.carryItToAppropriateStore(StatesPorter.AT_THE_STOREROOM);
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
