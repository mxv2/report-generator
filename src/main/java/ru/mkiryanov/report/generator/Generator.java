package ru.mkiryanov.report.generator;

import ru.mkiryanov.report.generator.exception.GeneratorException;
import ru.mkiryanov.report.generator.formatter.TextBoundaryFormatter;
import ru.mkiryanov.report.generator.formatter.TextFormatter;
import ru.mkiryanov.report.io.reader.Reader;
import ru.mkiryanov.report.io.reader.TabDelimitedFileReader;
import ru.mkiryanov.report.model.Column;
import ru.mkiryanov.report.model.Report;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: maxim-kiryanov
 * Time: 18.07.16 22:48
 */
public class Generator {
	private ModelProvider modelProvider;

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

	public static Context newContext() {
		Context context = new Context();
		context.setCharsetName("UTF-16");
		URL url = Generator.class.getResource("/source-data.tsv");
		context.setDataFile(url.getFile());
		context.setOutputFile("report.txt");

		context.setPageWidth(32);
		context.setPageHeight(12);

		List<Column> columns = new ArrayList<>();
		columns.add(new Column(1, "Номер", 8));
		columns.add(new Column(2, "Дата", 7));
		columns.add(new Column(3, "ФИО", 7));
		context.setColumns(columns);

		return context;
	}

	public static void main(String[] args) {
		new Generator().generate(newContext());
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
}
