package ru.mkiryanov.report.generator;

import ru.mkiryanov.report.generator.exception.GeneratorException;
import ru.mkiryanov.report.generator.formatter.TextBoundaryFormatter;
import ru.mkiryanov.report.generator.formatter.TextFormatter;
import ru.mkiryanov.report.io.reader.Reader;
import ru.mkiryanov.report.io.reader.TabDelimitedFileReader;
import ru.mkiryanov.report.model.Column;
import ru.mkiryanov.report.model.ModelProvider;
import ru.mkiryanov.report.model.Report;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

/**
 * User: maxim-kiryanov
 * Time: 18.07.16 22:48
 */
public class Generator {
	private ModelProvider modelProvider;

	public void generate(final Context context) {
		ModelProvider modelProvider = new ModelProvider(context, getReader(context), getInputStream(context));

		Report reportModel = modelProvider.getModel();
		final Map<Column, TextFormatter> formatters = createFormatters(context);

		for (int i = 0; i < reportModel.getRowsCount(); i++) {
			List<List<String>> formattedContents = formatContents(context, reportModel.getRow(i), formatters);


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

	private Map<Column, TextFormatter> createFormatters(Context context) {
		Map<Column, TextFormatter> formatterToColumns = new HashMap<>();

		for (Column column : context.getColumns()) {
			formatterToColumns.put(column, new TextBoundaryFormatter(column.getWidth()));
		}

		return formatterToColumns;
	}

	private List<List<String>> formatContents(Context context, Map<Column, String> row, Map<Column, TextFormatter> formatters) {
		List<List<String>> contents = new ArrayList<>(row.size());

		for (Column column : context.getColumns()) {
			String content = row.get(column);
			TextFormatter formatter = formatters.get(column);
			contents.add(formatter.formatToStringList(content));
		}

		return contents;
	}

	public static class Context {
		private String charsetName;
		private String dataFile;

		private int pageWidth;
		private int pageHeight;

		private List<Column> columns = new ArrayList<>();

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
	}
}
