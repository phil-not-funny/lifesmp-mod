package com.pnf.lifeanarchy.commands.autocompletes;

public interface CommandArgument {
	default public boolean match(String x) {
		return x.equals(this.toString());
	}
}
