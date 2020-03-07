package monitors;

import Interfaces.ArrivalLoungeInterfacePassenger;
import airport.Passenger;

public class ArrivalLounge implements ArrivalLoungeInterfacePassenger{
	
	public void whatShouldIDo(Passenger P) {
		if(P.getDest()) {
			
			P.goHome();
			
		}else {
			
			if(P.HasBag()) {
				
				P.goCollectABag();
				
			}else {
				
				P.takeABus();
				
			}
		}
	}
}



