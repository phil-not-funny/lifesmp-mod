package com.pnf.lifeanarchy.commands.autocompletes;

public enum BoogeyCommandArgument {
	start, end, cure;
	
	public boolean match(String x) {
		return x.equals(this.toString());
	}
}
