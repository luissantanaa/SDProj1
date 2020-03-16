
package airport;

public class Logger{
    Porter porter;
    Plane plane;
    BusDriver busdriver;
    
    int CB=0;
    int CR=0;
    
    volatile boolean end = false;
    public Logger(){
        this.plane = null;
    }
    
    
    public void toPrint() {
    	System.out.println("PLANE   PORTER          DRIVER          ");
        System.out.println(plane.getPlaneId() + " " + plane.getBags().size()+"	"
        				+ porter.getStateInteger() + " " + CB + " " + CR );
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
    
    
    public void incrementCB() {
    	CB++;
    }
    public void decrementCB() {
    	CB--;
    }
    
	public void incrementCR() {
		CR++;
    }
    public void decrementCR() {
    	CR--;
    }
    
    
}
