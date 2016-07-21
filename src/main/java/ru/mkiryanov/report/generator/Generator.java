package ru.mkiryanov.report.generator;

import ru.mkiryanov.report.generator.exception.GeneratorException;
import ru.mkiryanov.report.generator.formatter.TextBoundaryFormatter;
import ru.mkiryanov.report.generator.formatter.TextFormatter;
import ru.mkiryanov.report.io.reader.Reader;
import ru.mkiryanov.report.io.reader.TabDelimitedFileReader;
import ru.mkiryanov.report.model.Column;
import ru.mkiryanov.report.model.Report;
import ru.mkiryanov.report.settings.Settings;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

/**
 * User: maxim-kiryanov
 * Time: 18.07.16 22:48
 */
public class Generator {
	public void generate(final Context context) {
		try (InputStream in = getInputStream(context);
		     PrintWriter pw = getOutputStream(context)) {
			ModelProvider modelProvider = new ModelProvider(context, getReader(context), in);

			Report reportModel = modelProvider.getModel();
			Printer printer = new Printer(context, createFormatters(context), pw);

			for (int i = 0; i < reportModel.getRowsCount(); i++) {
				printer.print(reportModel.getRow(i));
			}
		} catch (Exception e) {
			throw new GeneratorException("Error", e);
		}
	}

	private Reader getReader(Context context) {
		return new TabDelimitedFileReader(context.getCharsetName());
	}

	private InputStream getInputStream(Context context) {
		try {
			return new BufferedInputStream(new FileInputStream(context.getDataFile()));
		} catch (IOException e) {
			throw new GeneratorException("Input data file not available", e);
		}
	}

	private PrintWriter getOutputStream(Context context) {
		try {
			return new PrintWriter(new OutputStreamWriter(
					new FileOutputStream(context.getOutputFile()), context.getCharsetName()), true);
		} catch (Exception e) {
			throw new GeneratorException("Problem with output", e);
		}
	}

	private Map<Column, TextFormatter> createFormatters(Context context) {
		Map<Column, TextFormatter> formatterToColumns = new HashMap<>();

		for (Column column : context.getColumns()) {
			formatterToColumns.put(column, new TextBoundaryFormatter(column.getWidth()));
		}

		return formatterToColumns;
	}

	public static Context newContext(Settings settings, String inputFileName,
	                                 String outputFileName, String charsetName) {
		Context context = new Context();
		context.setCharsetName(charsetName);
		context.setDataFile(inputFileName);
		context.setOutputFile(outputFileName);

		context.setPageWidth(settings.getPage().getWidth());
		context.setPageHeight(settings.getPage().getHeight());

		context.setColumns(IntStream.range(0, settings.getColumns().size())
				.mapToObj(i -> {
					Settings.Column columnSettings = settings.getColumns().get(i);
					return new Column(i, columnSettings.getTitle(), columnSettings.getWidth());
				})
				.collect(toList()));

		return context;
	}

	public static class Context {
		private String charsetName;
		private String dataFile;

		private int pageWidth;
		private int pageHeight;

		private List<Column> columns = new ArrayList<>();
		private String outputFile;

		public String getCharsetName() {
			return charsetName;
		}

		public void setCharsetName(String charsetName) {
			this.charsetName = charsetName;
		}

		public String getDataFile() {
			return dataFile;
		}

		public void setDataFile(String dataFile) {
			this.dataFile = dataFile;
		}

		public int getPageWidth() {
			return pageWidth;
		}

		public void setPageWidth(int pageWidth) {
			this.pageWidth = pageWidth;
		}

		public int getPageHeight() {
			return pageHeight;
		}

		public void setPageHeight(int pageHeight) {
			this.pageHeight = pageHeight;
		}

		public List<Column> getColumns() {
			return columns;
		}

		public void setColumns(List<Column> columns) {
			this.columns = columns;
		}

		public String getOutputFile() {
			return outputFile;
		}

		public void setOutputFile(String outputFile) {
			this.outputFile = outputFile;
		}
	}

	private static final String DEFAULT_CHARSET_NAME = "UTF-16";
	private static final String USAGE_MESSAGE = "Usage: Generator [settings file] [source file] " +
			"[result file] [charset (default=\"" + DEFAULT_CHARSET_NAME + "\")]?";
	private static final String FINAL_MESSAGE_TEMPLATE = "Report was generated (%.3f ms)";

	public static void main(String[] args) {
		if (args.length < 3) {
			System.out.println(USAGE_MESSAGE);
			System.exit(1);
		}

		String settingsFileName = args[0];
		String sourceFileName = args[1];
		String resultFileName = args[2];

		long start = System.nanoTime();
		Settings settings = null;
		try {
			settings = Settings.read(settingsFileName);
		} catch (JAXBException e) {
			System.out.println("Can not read settings:\n" + e);
			System.exit(1);
		}

		new Generator().generate(
				newContext(settings, sourceFileName, resultFileName, getCharsetOrDefault(args)));

		System.out.println(String.format(FINAL_MESSAGE_TEMPLATE, elapsed(start)));
		System.exit(0);
	}

	private static String getCharsetOrDefault(String... args) {
		return args.length > 3 ? args[3] : DEFAULT_CHARSET_NAME;
	}

	private static double elapsed(long start) {
		return (System.nanoTime() - start) / 1_000_000.0;
	}
}
