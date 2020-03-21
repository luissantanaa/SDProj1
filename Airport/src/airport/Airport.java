package airport;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Queue;
import Interfaces.*;
import monitors.*;
import java.util.Random;

public class Airport {

	public static void main(String[] args) {

		int nPlanes = 5;
		int nPassengers = 6;
		
		
		Plane[] planes = new Plane[nPlanes];
		Passenger[][] passengers = new Passenger[nPlanes][nPassengers];
		
		
		//LOGGER
		Logger log = new Logger();
		log.printInit();
				
		//BUS
		Bus bus = new Bus(3);


		//Monitors
		ArrivalLounge arrivalmonitor = new ArrivalLounge(log);
		BaggageCollectPoint baggagecollectpoint = new BaggageCollectPoint(log);
		ArrivalTransferTerm arrivaltransferterm = new ArrivalTransferTerm(bus,log);
		BaggageReclaimOffice baggagereclaimoffice = new BaggageReclaimOffice(log); 
		DepartureTransTerm  departurearrivalterm = new  DepartureTransTerm(bus,log);
		DepartureTermEntr departuretermentr = new DepartureTermEntr();
		TempStorageArea tempstoragearea = new TempStorageArea(log);
 
		//ArrivalTermExit arrivaltermexit = new ArrivalTermExit();

		//Resources 
	
		
		
		//BusDriver
		BusDriver BD = new BusDriver( (DepartureTransTermBDriverInterface) departurearrivalterm,
									(ArrivalTransferTermBDriverInterface) arrivaltransferterm, log);
		

		//Passenger
		for(int n = 0; n<nPlanes; n++){
			Queue<Passenger> passengerQueue = new LinkedList<>();
			Queue<Bag> BagQueue = new LinkedList<>();
		
			for(int i=0;i<nPassengers;i++){
				int rand = new Random().nextInt(4);
				if(rand==0){
						passengers[n][i] =  new Passenger(log, 6*n+i,null,false,(ArrivalLoungeInterfacePassenger) arrivalmonitor, 
							(BaggageCollectPointPassengerInterface) baggagecollectpoint,
							(ArrivalTransferTermPassengerInterface) arrivaltransferterm,
							(BaggageReclaimOfficePassengerInterface) baggagereclaimoffice,
							(DepartureTransTermPassengerInterface) departurearrivalterm,
							(DepartureTermEntrancePassengerInterface) departuretermentr);
				}else{
						passengers[n][i] = new Passenger(log,6*n+i,null,false,(ArrivalLoungeInterfacePassenger) arrivalmonitor, 
							(BaggageCollectPointPassengerInterface) baggagecollectpoint,
							(ArrivalTransferTermPassengerInterface) arrivaltransferterm,
							(BaggageReclaimOfficePassengerInterface) baggagereclaimoffice,
							(DepartureTransTermPassengerInterface) departurearrivalterm,
							(DepartureTermEntrancePassengerInterface) departuretermentr);
				}
				int nBags = new Random().nextInt(3);
				List<Bag> listbag = new ArrayList<Bag>();
				for(int nb = 1; nb<=nBags; nb++){
					Bag b = new Bag(i+n,  passengers[n][i].isDest(), nb);
					listbag.add(b);
					BagQueue.add(b);
				}
				passengers[n][i].setB(listbag);
				
				passengerQueue.add(passengers[n][i]);
				
			}	
			//PLANE
			planes[n] = new Plane(passengerQueue,n,BagQueue);	
		}

		Porter porter = new Porter(log,(ArrivalLoungeInterfacePorter) arrivalmonitor,
			(BaggageCollectPointPorterInterface) baggagecollectpoint,
			(TempStorageInterfacePorter) tempstoragearea);
			

		log.setBusDriver(BD);
		log.setPorter(porter);
		log.setBus(bus);
	
		porter.start();
		BD.start();

		for(int n=0; n<nPlanes;n++){
			arrivalmonitor.setPlane(planes[n]);
			baggagecollectpoint.moreBags();
			int i=0;
			log.setPlane(planes[n]);
			while(!planes[n].isEmpty()) {
				Passenger p = planes[n].removePassenger();
				//log.addPassengers(p);
				p.start();
			}
			
			
			
			
			for(int y=0;y<nPassengers;y++){
				
				try {
				
					passengers[n][y].join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			
			log.resetPassenger();
		}
		arrivalmonitor.lastFlight();
		arrivaltransferterm.dayWordEnd();
		
		try {
			porter.join();
			BD.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		log.finalPrint();
	}

} 
