package airport;


import Interfaces.ArrivalLoungeInterfacePorter;
import Interfaces.BaggageCollectPointPorterInterface;
import Interfaces.TempStorageInterfacePorter;
import states.StatesPorter;

public class Porter {
	private ArrivalLoungeInterfacePorter arrivalmonitor;
	private BaggageCollectPointPorterInterface baggageCollectPointMonitor;
	private Bag b;
	private Plane plane;
	private StatesPorter state;
	private TempStorageInterfacePorter tempstoragemonitor;
	public Porter(Bag b, Plane plane, ArrivalLoungeInterfacePorter arrivalmonitor, BaggageCollectPointPorterInterface baggageCollectPointMonitor, TempStorageInterfacePorter tempstoragemonitor) {
		super();
		this.b = b;
		this.plane = plane;
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
	public StatesPorter getState() {
		return state;
	}
	
	
	public void run() {
		
		switch(this.state) {
			case WAITING_FOR_A_PLANE_TO_LAND:
				if(arrivalmonitor.takeARest() && this.plane.getBags().size() > 0){
					this.tryToCollectABag();
				}
				break;
			case AT_THE_PLANES_HOLD:
				this.b = this.arrivalmonitor.collectBag();
				if(this.b == null) {
					this.noMoreBagsToCollect();
					break;
				}
				if(this.b.isDest()) {
					this.baggageCollectPointMonitor.addBag(this.b);
					this.carryItToAppropriateStore(StatesPorter.AT_THE_LUGGAGE_BELT_CONVEYOR);
				}else {
					this.tempstoragemonitor.addBag(this.b);
					this.carryItToAppropriateStore(StatesPorter.AT_THE_STOREROOM);
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
	
	public void tryToCollectABag() {
		this.state = StatesPorter.AT_THE_PLANES_HOLD;
	}
	
	public void carryItToAppropriateStore(StatesPorter state) {
		this.state = state;
	}
	
	public void noMoreBagsToCollect() {
		this.state = StatesPorter.WAITING_FOR_A_PLANE_TO_LAND;
	}
	
}
