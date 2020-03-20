
package airport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import states.StatesPerson;

public class Logger{
    Porter porter;
    Plane plane;
    BusDriver busdriver;
    List <Passenger> passenger;
    private final ReentrantLock lock = new ReentrantLock();
    int CB=0;
    int CR=0;
    int lBags = 0;
    
    volatile boolean end = false;
    public Logger(){
        this.plane = null;
        this.passenger = new ArrayList<Passenger>();
    }
    
    public void printInit() {
    	
    	System.out.print("IRPORT RHAPSODY - Description of the internal state of the problem\r\n" + 
    			"\r\n" + 
    			"PLANE    PORTER                  DRIVER\r\n" + 
    			"FN BN  Stat CB SR   Stat  Q1 Q2 Q3 Q4 Q5 Q6  S1 S2 S3\r\n" + 
    			"                                                         PASSENGERS\r\n" + 
    			"St1 Si1 NR1 NA1 St2 Si2 NR2 NA2 St3 Si3 NR3 NA3 St4 Si4 NR4 NA4 St5 Si5 NR5 NA5 St6 Si6 NR6 NA6\n");
    }
    
    public void toPrint() {
    	lock.lock();
		try {
				System.out.println(plane.getPlaneId() + " " + plane.getBags().size()+"  	"
						+ porter.getString() + " " + CB + " " + CR + "");
				
				for(int i = 0; i<6; i++) {
					if(passenger.size()>i) {
						System.out.print(passenger.get(i).getString());
					}else {
						System.out.print(" --- --- - - ");
					}
				}
				System.out.println("");
		}finally {
			lock.unlock();
		}	
    	
    	
    }
    public void setPlane(Plane plane){
        this.plane=plane;
        toPrint();
    }
    public void setPorter(Porter porter) {
    	this.porter=porter;
    }
    public void setBusDriver(BusDriver busdriver) {
    	this.busdriver=busdriver;
    }
    
    
    public void addPassengers(Passenger p) {
    	lock.lock();
		try {
			passenger.add(p);
		}finally {
			lock.unlock();
		}	
    	
    	
    }
    
    
    public void finalPrint() {
    	System.out.print("\n\nFinal report\r\n" + 
    			"N. of passengers which have this airport as their final destination = ##\r\n" + 
    			"N. of passengers in transit = ##\r\n" + 
    			"N. of bags that should have been transported in the the planes hold = ##\r\n" + 
    			"N. of bags that were lost ="+ lBags);
    }
    public void resetPassenger() {
    	passenger.clear();
    }

    public void incrementCB() {
    	CB++;
    }
    public void decrementCB() {
    	CB--;
    }
    public void incrementLost() {
    	lBags++;
    }
    
	public void incrementCR() {
		CR++;
    }
    public void decrementCR() {
    	CR--;
    }
    
    
}
