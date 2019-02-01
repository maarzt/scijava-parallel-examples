
package org.scijava.parallel.utils;

import java.util.Arrays;

import org.scijava.parallel.ParallelService;
import org.scijava.parallel.ParallelizationParadigm;
import org.scijava.parallel.ParallelizationParadigmProfile;

import cz.it4i.parallel.ImageJServerParadigm;

public class StartImageJServer {

	public static ParallelizationParadigm getTestParadigm(
		ParallelService parallelService)
	{
		parallelService.deleteProfiles();
		parallelService.addProfile(new ParallelizationParadigmProfile(
			ImageJServerParadigm.class, "lonelyBiologist01"));
		parallelService.selectProfile("lonelyBiologist01");

		ParallelizationParadigm paradigm = parallelService.getParadigm();
		((ImageJServerParadigm) paradigm).setHosts(Arrays.asList("localhost:8080"));
		paradigm.init();
		return paradigm;

	}

}
