
package org.scijava.parallel.examples;

import java.util.Collections;
import java.util.concurrent.Executors;

import net.imglib2.FinalInterval;
import net.imglib2.Interval;

import org.scijava.Context;
import org.scijava.parallel.ParallelService;
import org.scijava.parallel.ParallelizationParadigm;
import org.scijava.parallel.utils.StartImageJServer;

public class ExampleExecute {

	public static void main(String... args) throws Exception {
		Context context = new Context();
		Executors.newFixedThreadPool(1).submit(() -> {
			try {
				net.imagej.server.Main.main(new String[] {});
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		Thread.sleep(2000);
		try (ParallelizationParadigm paradigm = StartImageJServer.getTestParadigm(
			context.service(ParallelService.class)))
		{
			Interval result = (Interval) paradigm.runAll(ExampleCommand.class,
				Collections.singletonList(Collections.singletonMap("interval",
					new FinalInterval(10, 20)))).get(0).get("outInterval");
			System.err.println("Interval from server: " + result);
		}
		System.exit(0);
	}
}
