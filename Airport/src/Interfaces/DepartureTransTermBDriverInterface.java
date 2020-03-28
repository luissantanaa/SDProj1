package Interfaces;

/**
 * 
 * Interface de acesso do condutor do autocarro ao Monitor Departure Transfer Terminal
 *
 */

public interface DepartureTransTermBDriverInterface {
	public void waitForPassengers();
	public void arriveDepTransTerm();
	public void leaveDepTransTerm();
	
}

