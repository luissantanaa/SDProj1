package airport;

/**
 * Ficheiro principal
 * Autores: 
 * João Teixeira
 * Luís Santana 80304
 */

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Queue;
import Interfaces.*;
import monitors.*;
import java.util.Random;
	/**
	 * 
	 * Classe principal
	 *
	 */
public class Airport {

	/**
	 * @param int nPlanes representa nrº de voos a realizar \n
	 * @param int nPassengers representa nrº de passageiros em cada avião
	 * @param Plane[] planes array que guarda os aviões a ser usados na simulação
	 * @param Passenger[][] passengers array que guarda os passageiros e o seu respetivo avião
	 */
	public static void main(String[] args) {

		int nPlanes = 5; // nrº de aviões
		int nPassengers = 6; // nrº de passageiros por avião
		
		
		Plane[] planes = new Plane[nPlanes]; //aviões a ser usados
		Passenger[][] passengers = new Passenger[nPlanes][nPassengers]; //passageiros e o seu respetivo avião
		
		
		//LOGGER
		Logger log = new Logger(); 	//instanciação do Logger e print inicial
		log.printInit();
				
		//BUS
		Bus bus = new Bus(3); //instanciação do autocarro 


		//Monitors
		//construtores dos varios monitores utilizados ao longo da simulação
		
		BaggageCollectPoint baggagecollectpoint = new BaggageCollectPoint(log);
		ArrivalTransferTerm arrivaltransferterm = new ArrivalTransferTerm(bus,log);
		BaggageReclaimOffice baggagereclaimoffice = new BaggageReclaimOffice(log); 
		DepartureTransTerm  departurearrivalterm = new  DepartureTransTerm(bus,log);
		DepartureTermEntr departuretermentr = new DepartureTermEntr();
		TempStorageArea tempstoragearea = new TempStorageArea(log);
		ArrivalTermExit arrivaltermexit = new ArrivalTermExit();
		ArrivalLounge arrivalmonitor = new ArrivalLounge(log,departuretermentr,arrivaltermexit);
		//Resources 
	
		
		
		//BusDriver
		//instanciação do condutor do autocarro usado na simulação
		BusDriver BD = new BusDriver( (DepartureTransTermBDriverInterface) departurearrivalterm,
									(ArrivalTransferTermBDriverInterface) arrivaltransferterm,
									(ArrivalLoungeInterfaceBDriver) arrivalmonitor, log);
		

		//instanciação dos passageiros e das suas malas para o numero de aviões selecionado em cima
		for(int n = 0; n<nPlanes; n++){
			Queue<Passenger> passengerQueue = new LinkedList<>();//fila de passageiros
			Queue<Bag> BagQueue = new LinkedList<>();	//fila de malas
		
			for(int i=0;i<nPassengers;i++){
				int rand = new Random().nextInt(2); //geração de um pseudointeiro entre [0,2[ para decidir se o passageiro esta no destino 
				if(rand==0){						//ou se tem que ir para outro voo
						passengers[n][i] =  new Passenger(log, 6*n+i,null,true,(ArrivalLoungeInterfacePassenger) arrivalmonitor, 
							(BaggageCollectPointPassengerInterface) baggagecollectpoint,
							(ArrivalTransferTermPassengerInterface) arrivaltransferterm,
							(BaggageReclaimOfficePassengerInterface) baggagereclaimoffice,
							(DepartureTransTermPassengerInterface) departurearrivalterm,
							(DepartureTermEntrancePassengerInterface) departuretermentr,
							(ArrivalTermExitPassengerInterface) arrivaltermexit);
				}else{
						passengers[n][i] = new Passenger(log,6*n+i,null,false,(ArrivalLoungeInterfacePassenger) arrivalmonitor, 
							(BaggageCollectPointPassengerInterface) baggagecollectpoint,
							(ArrivalTransferTermPassengerInterface) arrivaltransferterm,
							(BaggageReclaimOfficePassengerInterface) baggagereclaimoffice,
							(DepartureTransTermPassengerInterface) departurearrivalterm,
							(DepartureTermEntrancePassengerInterface) departuretermentr,
							(ArrivalTermExitPassengerInterface) arrivaltermexit);
				}
				int nBags = new Random().nextInt(3);//geração de um pseudointeiro entre [0,3[ para decidir o nrº de malas de cada passageiro
				List<Bag> listbag = new ArrayList<Bag>(); //lista de malas de cada passageiro
				for(int nb = 1; nb<=nBags; nb++){
					Bag b = new Bag(i+n,  passengers[n][i].isDest(), nb); //instanciação de cada mala
					listbag.add(b);
					BagQueue.add(b);
				}
				passengers[n][i].setB(listbag); //associar lista de malas ao seu dono
				
				passengerQueue.add(passengers[n][i]); //adicionar passageiro a fila de passageiros do respetivo aviao
				
			}	
			//PLANE
			planes[n] = new Plane(passengerQueue,n,BagQueue); //instanciação do avião usando a lista de passageiros e malas
		}

		Porter porter = new Porter(log,(ArrivalLoungeInterfacePorter) arrivalmonitor,	//instanciação do bagageiro usado na simulação
			(BaggageCollectPointPorterInterface) baggagecollectpoint,
			(TempStorageInterfacePorter) tempstoragearea);
			
		//passagem dos recursos necessários para o funcionamento do logger
		log.setBusDriver(BD);
		log.setPorter(porter);
		log.setBus(bus);
	
		porter.start(); //inicio do lifecycle das threads do bagageiro e do condutor do autocarro
		BD.start();

		for(int n=0; n<nPlanes;n++){ //cada avião é passado ao monitor e ao logger,é sinalizado que existem malas para recolher
			arrivalmonitor.setPlane(planes[n]);
			baggagecollectpoint.moreBags();
			log.setPlane(planes[n]);
			while(!planes[n].isEmpty()) { //enquanto houver passageiros no aviao,remove do avião e inicia o ciclo de vida do passageiro
				Passenger p = planes[n].removePassenger();
				p.start();
			}
			for(int y=0;y<nPassengers;y++){	
				try {
					passengers[n][y].join();	//termina a execução de todos os passageiros
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			log.resetPassenger(); //da reset aos passageiros no logger entre aviões
		}
		
		arrivalmonitor.lastFlight(); //sinaliza que foi o ultimo avião e que o dia acabou
		arrivaltransferterm.dayWorkEnd();
		
		try {
			porter.join(); //termina a execução do bagageiro e do condutor do autocarro
			BD.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		log.finalPrint();//print final do logger
	}

} 
