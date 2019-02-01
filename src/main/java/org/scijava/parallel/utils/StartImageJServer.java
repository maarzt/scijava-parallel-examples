
package org.scijava.parallel.utils;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.scijava.Context;
import org.scijava.command.CommandService;
import org.scijava.parallel.ParallelService;
import org.scijava.parallel.ParallelizationParadigm;
import org.scijava.parallel.ParallelizationParadigmProfile;

import cz.it4i.parallel.AbstractImageJServerRunner;
import cz.it4i.parallel.ImageJServerParadigm;
import cz.it4i.parallel.ui.RunImageJServerOnHPCCommand;

public class StartImageJServer {

	public static ParallelizationParadigm getTestParadigm(
		ParallelService parallelService)
	{
		return getTestParadigm(parallelService, Collections.singletonList(
			"localhost:8080"));
	}

	public static ParallelizationParadigm getTestParadigm(
		ParallelService parallelService, List<String> hosts)
	{
		parallelService.deleteProfiles();
		parallelService.addProfile(new ParallelizationParadigmProfile(
			ImageJServerParadigm.class, "lonelyBiologist01"));
		parallelService.selectProfile("lonelyBiologist01");

		ParallelizationParadigm paradigm = parallelService.getParadigm();
		((ImageJServerParadigm) paradigm).setHosts(hosts);
		paradigm.init();
		return paradigm;

	}

	public static ParallelizationParadigm getTestParadigm(
		ParallelService parallelService, AbstractImageJServerRunner runner)
	{
		return getTestParadigm(parallelService, runner.getPorts().stream().map(
			port -> "localhost:" + port).collect(Collectors.toList()));
	}

	public static AbstractImageJServerRunner getRunner(Context context)
		throws InterruptedException, ExecutionException
	{
		CommandService commandService = context.getService(CommandService.class);
		AbstractImageJServerRunner runner =
			(AbstractImageJServerRunner) commandService.run(
				RunImageJServerOnHPCCommand.class, true).get().getOutputs().get(
					"runner");
		runner.startIfNecessary();
		return runner;
	}

}
