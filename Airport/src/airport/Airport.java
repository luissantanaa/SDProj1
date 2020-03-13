package airport;

import java.util.ArrayList;
import java.util.List;

import Interfaces.*;
import monitors.*;

public class Airport {

	public static void main(String[] args) {
		//BUS
		Bus bus = new Bus(12);
		
		
		//Monitors
		ArrivalLounge arrivalmonitor = new ArrivalLounge();
		BaggageCollectPoint baggagecollectpoint = new BaggageCollectPoint();
		ArrivalTransferTerm arrivaltransferterm = new ArrivalTransferTerm(bus);
		BaggageReclaimOffice baggagereclaimoffice = new BaggageReclaimOffice(); 
		DepartureArrivalTerm  departurearrivalterm = new  DepartureArrivalTerm(bus);
		DepartureTermEntr departuretermentr = new DepartureTermEntr();
		TempStorageArea tempstoragearea = new TempStorageArea();
		
		//ArrivalTermExit arrivaltermexit = new ArrivalTermExit();
		
		//Resources 
		//bags
		Bag b = new Bag (0, false);
		List<Bag> listb = new ArrayList<Bag>();
		listb.add(b);
		
		
		
		//BusDriver
		
		//Plane
		//Plane plane = new Plane();
		//Porter
		
		//Passenger
		
		Passenger p = new Passenger(0,listb,false,(ArrivalLoungeInterfacePassenger) arrivalmonitor, 
				(BaggageCollectPointPassengerInterface) baggagecollectpoint,
				(ArrivalTransferTermPassengerInterface) arrivaltransferterm,
				(BaggageReclaimOfficePassengerInterface) baggagereclaimoffice,
				(DepartureArrivalTermPassengerInterface) departurearrivalterm,
				(DepartureTermEntrancePassengerInterface) departuretermentr);
	
		
		
		
		
		
		
		
	}

} 
