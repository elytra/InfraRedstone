package com.elytradev.infraredstone.api;

/**
 * This Capability describes an "inspection" signal value to InfraComparators.
 */
public interface IEncoderScannable {
	/**
	 * @return a value from 0-63 depending on the state of your object.
	 * It may be helpful to format the value in binaru: `0b00_0000`
	 */
	int getComparatorValue();
}
