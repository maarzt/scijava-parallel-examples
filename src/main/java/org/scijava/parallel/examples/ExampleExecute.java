package org.scijava.parallel.examples;

import net.imglib2.FinalInterval;
import org.scijava.Context;
import org.scijava.parallel.ParallelService;
import org.scijava.parallel.ParallelizationParadigm;
import org.scijava.parallel.utils.StartImageJServer;

import java.util.Collections;
import java.util.concurrent.Executors;

public class ExampleExecute {

	public static void main(String... args) throws Exception {
		Context context = new Context();
		Executors.newFixedThreadPool(1).submit(
				() -> {
					try {
						net.imagej.server.Main.main(new String[]{});
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
		);
		Thread.sleep(2000);
		try( ParallelizationParadigm paradigm = StartImageJServer.getTestParadigm(context.service(ParallelService.class)))
		{
			paradigm.runAll(Collections.singletonList(ExampleCommand.class), Collections.singletonList(Collections.singletonMap("interval", "Hello World!")));
		}
		System.exit(0);
	}
}
