
package org.scijava.parallel.examples;

import net.imglib2.Interval;

import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Command.class)
public class ExampleCommand implements Command {

	@Parameter
	private Interval interval;

	@Parameter(type = ItemIO.OUTPUT)
	private Interval outInterval;

	@Override
	public void run() {
		System.err.println("##############################################");
		System.err.println(interval);
		outInterval = interval;
		System.err.println("##############################################");
	}
}
