
package org.scijava.parallel.examples;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import net.imagej.Dataset;
import net.imagej.ImageJ;

import org.scijava.Context;
import org.scijava.io.IOService;
import org.scijava.parallel.ParallelService;
import org.scijava.parallel.ParallelizationParadigm;
import org.scijava.parallel.utils.StartImageJServer;

import cz.it4i.parallel.AbstractImageJServerRunner;

public class ExampleExecuteOnCluster {

	private static String URL_OF_IMAGE_TO_ROTATE =
		"https://upload.wikimedia.org/wikipedia/en/7/7d/Lenna_%28test_image%29.png";

	public static void main(String[] args) throws IOException {
		ImageJ imageJ = new ImageJ();
		imageJ.ui().showUI();
		Context context = imageJ.getContext();
		IOService ioService = context.getService(IOService.class);

		Path fileToRotate = getImageToRotate();
		Path outputDirectory = Paths.get("output");
		if (!Files.exists(outputDirectory)) {
			Files.createDirectories(outputDirectory);
		}

		Dataset ds = (Dataset) ioService.open(fileToRotate.toString());
		try (AbstractImageJServerRunner runner = StartImageJServer.getRunner(
			context);
				ParallelizationParadigm paradigm = StartImageJServer.getTestParadigm(
					context.service(ParallelService.class), runner))
		{
			Map<String, Object> params = new HashMap<>();
			params.put("dataset", ds);
			params.put("angle", 189.);
			ds = (Dataset) paradigm.runAll(
				"net.imagej.plugins.commands.imglib.RotateImageXY", Collections
					.singletonList(params)).get(0).get("dataset");
		}
		catch (InterruptedException | ExecutionException exc) {
			throw new RuntimeException(exc);
		}
		ioService.save(ds, outputDirectory.resolve("rotated.png").toString());
		Files.deleteIfExists(fileToRotate);
		System.exit(0);
	}

	final static protected Path getImageToRotate() {
		Path imageToRotate;
		try (InputStream is = new URL(URL_OF_IMAGE_TO_ROTATE).openStream()) {
			imageToRotate = Files.createTempFile("", URL_OF_IMAGE_TO_ROTATE.substring(
				URL_OF_IMAGE_TO_ROTATE.lastIndexOf('.')));
			Files.copy(is, imageToRotate, StandardCopyOption.REPLACE_EXISTING);
		}
		catch (IOException exc) {
			throw new RuntimeException(exc);
		}
		return imageToRotate;
	}
}
