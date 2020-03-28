package Interfaces;

/**
 * 
 * Interface de acesso do condutor do autocarro ao Monitor Arrival Transfer Terminal
 *
 */

public interface ArrivalTransferTermBDriverInterface {
	public boolean BusNotFull();
	public boolean hasDaysWorkEnded();
	public void announcingBusBoarding(int passNumber);
}
