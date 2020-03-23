
package airport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.io.File;  // Import the File class
import java.io.FileWriter;
import java.io.IOException; 

public class Logger{
	private Porter porter;
	private Plane plane;
	private BusDriver busdriver;
	private List <Passenger> passenger;
	private Bus bus;
	private List <Passenger> passengerWaiting;
	
	
	private String toFileString = "";
	
    private final ReentrantLock lock = new ReentrantLock();
    
    
    
    private int BagSize = 0;
    private int PassDest = 0;
    private int PassTransit = 0;
    private int CB=0;
    private int CR=0;
    private int lBags = 0;
    private String fileName;
    volatile boolean end = false;
    public Logger(){
        this.plane = null;
        this.passenger = new ArrayList<Passenger>();
        this.passengerWaiting = new ArrayList<Passenger>();
        toFileString = "";
        
        // Log FIle
        String nameFile = "airportLog";
        boolean exist = true;
        int i = 0;
        while(exist) {
	        try {
	            File myObj = new File(nameFile + i+".txt");
	            if (myObj.createNewFile()) {
	            	fileName = nameFile + i+".txt";
	            	exist = false;
	            } else {
	            	i++;
	            	exist = true;
	            }
	          } catch (IOException e) {
	            System.out.println("An error occurred.");
	            e.printStackTrace();
	          }
        }
        
    }
    
    public void setBus(Bus bus) {
    	this.bus = bus;
    }
    public void printInit() {
      
    		 toFileString +=("AIRPORT RHAPSODY - Description of the internal state of the problem\r\n" + 
    	    			"\r\n" + 
    	    			"PLANE    PORTER                  DRIVER\r\n" + 
    	    			"FN BN  Stat CB SR   Stat  Q1 Q2 Q3 Q4 Q5 Q6  S1 S2 S3\r\n" + 
    	    			"                                                         PASSENGERS\r\n" + 
    	    			"St1 Si1 NR1 NA1 St2 Si2 NR2 NA2 St3 Si3 NR3 NA3 St4 Si4 NR4 NA4 St5 Si5 NR5 NA5 St6 Si6 NR6 NA6\n");
    	System.out.print("AIRPORT RHAPSODY - Description of the internal state of the problem\r\n" + 
    			"\r\n" + 
    			"PLANE    PORTER                  DRIVER\r\n" + 
    			"FN BN  Stat CB SR   Stat  Q1 Q2 Q3 Q4 Q5 Q6  S1 S2 S3\r\n" + 
    			"                                                         PASSENGERS\r\n" + 
    			"St1 Si1 NR1 NA1 St2 Si2 NR2 NA2 St3 Si3 NR3 NA3 St4 Si4 NR4 NA4 St5 Si5 NR5 NA5 St6 Si6 NR6 NA6\n");
    }
    
    public void toPrint() {
       	boolean canPrint=true;
    	lock.lock();
    	try {
    		while(canPrint) {
        		try {
            		
          	        toFileString += (plane.getPlaneId() + " " + plane.getBags().size()+"  	"
        	  				+ porter.getString() + " " + CB + " " + CR + "	"+ busdriver.getString() +"	"+printWait()+"	" +bus.toPrint() + "\n");
        	  	      
        	  	    System.out.println(plane.getPlaneId() + " " + plane.getBags().size()+"  	"
        					+ porter.getString() + " " + CB + " " + CR + "	"+ busdriver.getString() +"	"+printWait()+"	" +bus.toPrint());
        			
        			for(int i = 0; i<6; i++) {
        				if(passenger.size()>i) {
        					 toFileString+=(passenger.get(i).getString());
        					 System.out.print(passenger.get(i).getString());
        				}else {
        					toFileString+=(" --- --- - - ");
        					System.out.print(" --- --- - - ");
        				}
        			}
        			 toFileString+=("\n");
        			 System.out.println("");
        			 canPrint=false;
        	  	
            	}catch (IndexOutOfBoundsException | java.lang.NullPointerException | IllegalMonitorStateException e ){
            	}
    		}
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
    
    
    public void addPassengersWaiting(Passenger p) {
    	lock.lock();
		try {
			passengerWaiting.add(p);
		}finally {
			lock.unlock();
		}	
    	
    	
    }
    
    
    public void removePassengersWaiting(Passenger p) {
    	lock.lock();
		try {
			passengerWaiting.remove(p);
		}finally {
			lock.unlock();
		}	
    	
    	
    }
    
    
    public String printWait() {
    	String s = "";
    	for(int i=0; i<6; i++) {
    		if(i<passengerWaiting.size()) {
    			s += " " + passengerWaiting.get(i).getPId();
    		}else {
    			s += " -";
    		}
    	}
    	return s;
    }
    
    public void finalPrint() {
    	System.out.print("\n\nFinal report\r\n" + 
    			"N. of passengers which have this airport as their final destination = " + PassDest + "\r\n" + 
    			"N. of passengers in transit = " + PassTransit + "\r\n" + 
    			"N. of bags that should have been transported in the the planes hold = " + this.BagSize +"\r\n" + 
    			"N. of bags that were lost = "+ lBags);
    	
    	toFileString += ("\n\nFinal report\r\n" + 
    			"N. of passengers which have this airport as their final destination = " + PassDest + "\r\n" + 
    			"N. of passengers in transit = " + PassTransit + "\r\n" + 
    			"N. of bags that should have been transported in the the planes hold = " + this.BagSize +"\r\n" + 
    			"N. of bags that were lost = "+ lBags);
    	
    	 try {
    	      FileWriter myWriter = new FileWriter(fileName);
    	      myWriter.write(toFileString);
    	      myWriter.close();
    	      System.out.println("\nSuccessfully wrote to the file.");
    	    } catch (IOException e) {
    	      System.out.println("An error occurred.");
    	      e.printStackTrace();
    	    }
    }
    
    public void setBagSizes(int size) {
    	this.BagSize = this.BagSize  +  size;
    }
    
    public void incPassDest() {
    	PassDest++;
    }
    
    public void incPassTransit() {
    	PassTransit++;
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
