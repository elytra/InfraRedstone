package com.elytradev.infraredstone.logic.impl;

import com.elytradev.infraredstone.logic.IEncoderScannable;
import com.elytradev.infraredstone.logic.IInfraRedstone;

public class InfraRedstoneHandler implements IInfraRedstone, IEncoderScannable {
	public static final IInfraRedstone ALWAYS_OFF = new IInfraRedstone() {
		@Override
		public int getSignalValue() { return 0; }
	};
	
	public static final IInfraRedstone ALWAYS_MAX = new IInfraRedstone() {
		@Override
		public int getSignalValue() { return 63; }
	};
	
	private int signalValue;
	private int nextSignalValue;
	private Runnable onChanged;
	
	public void listen(Runnable r) {
		this.onChanged = r;
	}
	
	@Override
	public int getSignalValue() {
		return signalValue;
	}
	
	public void setSignalValue(int val) {
		signalValue = val;
		onChanged();
	}
	
	public int getNextSignalValue() {
		return nextSignalValue;
	}
	
	public void setNextSignalValue(int val) {
		nextSignalValue = val;
		onChanged();
	}

	@Override
	public int getComparatorValue() {
		return signalValue;
	}
	
	public void onChanged() {
		if (onChanged!=null) onChanged.run();
	}
}
