package com.elytradev.infraredstone.api;

/**
 * This Capability describes an "inspection" signal value to InfraComparators.
 */
public interface IEncoderScannable {
	/**
	 * @return a value from 0-63 depending on the state of your object.
	 * See the Encoder Guidelines page on the InfraRedstone wiki for usage examples.
	 * It may be helpful to format the value in binary: `0b00_0000`
	 */
	int getComparatorValue();
}
