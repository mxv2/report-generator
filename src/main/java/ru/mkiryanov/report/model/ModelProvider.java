package ru.mkiryanov.report.model;

import ru.mkiryanov.report.generator.Generator;
import ru.mkiryanov.report.io.reader.Reader;

import java.io.InputStream;
import java.util.List;

/**
 * User: maxim-kiryanov
 * Time: 18.07.16 22:50
 */
public class ModelProvider {
	private Generator.Context context;
	private Reader reader;
	private InputStream in;

	public ModelProvider(Generator.Context context, Reader reader, InputStream in) {
		this.context = context;
		this.reader = reader;
		this.in = in;
	}

	public Report getModel() {
		reader.read(in);

		Report report = new Report();
		List<Column> columns = context.getColumns();
		long rowIndex = 0;
		for (String[] line : reader.getLines()) {
			if (line.length != columns.size()) {
				throw new RuntimeException("Malformed data");
			}

			for (int colIndex = 0; colIndex < line.length; colIndex++) {
				report.setCellString(rowIndex, columns.get(colIndex), line[colIndex]);
			}

			rowIndex++;
		}

		return report;
	}
}
