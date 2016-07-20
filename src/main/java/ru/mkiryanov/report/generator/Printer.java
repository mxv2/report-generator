package ru.mkiryanov.report.generator;

import ru.mkiryanov.report.generator.formatter.TextFormatter;
import ru.mkiryanov.report.model.Column;

import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

/**
 * User: maxim-kiryanov
 * Time: 20.07.16 20:10
 */
public class Printer {
	private final String rowDelimeter;
	private final String pageDelimeter;
	private final String rowTemplate;
	private int pageWidth;
	private int pageHeight;

	private Generator.Context context;
	private PrintWriter writer;
	private Map<Column, TextFormatter> textFormatters;

	private StringBuilder formatterBuilder = new StringBuilder();
	private Formatter formatter = new Formatter(formatterBuilder);
	private Map<Column, List<String>> headerContents;
	private int headerDeepSize;

	private boolean isNewPage;
	private int currentRowPosition;

	public Printer(Generator.Context context, Map<Column, TextFormatter> textFormatters, PrintWriter writer) {
		this.textFormatters = textFormatters;
		this.writer = writer;
		this.context = context;

		rowDelimeter = new String(new char[context.getPageWidth()]).replace('\0', '-');
		pageDelimeter = "~";
		rowTemplate = createRowTemplate(context);

		Map<Column, String> headerContents = new HashMap<>();
		for (Column column : context.getColumns()) {
			headerContents.put(column, column.getTitle());
		}
		this.headerContents = formatContents(headerContents, context, textFormatters);

		isNewPage = true;
		currentRowPosition = 0;

		pageHeight = context.getPageHeight();
		pageWidth = context.getPageWidth();
	}

	private String createRowTemplate(Generator.Context context) {
		StringBuilder rowTemplateBuilder = new StringBuilder();
		for (Column column : context.getColumns()) {
			rowTemplateBuilder
					.append("| %")
					.append(column.getWidth())
					.append("s ");
		}

		return rowTemplateBuilder.append("|").toString();
	}

	public void print(Map<Column, String> row) {
		Map<Column, List<String>> contents = formatContents(row, context, textFormatters);
		int deepSize = findMaxSize(contents);

		if (currentRowPosition + deepSize >= pageHeight) {
			writer.println(pageDelimeter);
			currentRowPosition++;
			isNewPage = true;
		}

		if (isNewPage) {
			isNewPage = false;
			currentRowPosition = 0;

			currentRowPosition += printHeader();
			writer.println(rowDelimeter);
			currentRowPosition++;
		}

		currentRowPosition += printRow(contents, deepSize);
		writer.println(rowDelimeter);
		currentRowPosition++;
	}

	private int printRow(Map<Column, List<String>> contents, int deepSize) {
		String[] strings = new String[context.getColumns().size()];

		for (int rowIndex = 0; rowIndex < deepSize; rowIndex++) {
			for (int colIndex = 0; colIndex < strings.length; colIndex++) {
				Column column = context.getColumns().get(colIndex);
				strings[colIndex] = getOrDefault(contents.get(column), rowIndex);
			}
			formatterBuilder.setLength(0);
			writer.println(formatter.format(rowTemplate, strings));
		}

		return deepSize;
	}

	private int findMaxSize(Map<Column, List<String>> contents) {
		int maxSize = 0;

		for (Map.Entry<Column, List<String>> entry : contents.entrySet()) {
			int size = entry.getValue().size();
			if (maxSize < size) {
				maxSize = size;
			}
		}

		return maxSize;
	}

	private String getOrDefault(List<String> strings, int index) {
		return index < strings.size() ? strings.get(index) : "";
	}

	private int printHeader() {
		return printRow(headerContents, findMaxSize(headerContents));
	}

	private Map<Column, List<String>> formatContents(Map<Column, String> row, Generator.Context context,
	                                          Map<Column, TextFormatter> formatters) {
		Map<Column, List<String>> contents = new HashMap<>(row.size());

		for (Column column : context.getColumns()) {
			String content = row.get(column);
			TextFormatter formatter = formatters.get(column);
			contents.put(column, formatter.formatToStringList(content));
		}

		return contents;
	}
}
