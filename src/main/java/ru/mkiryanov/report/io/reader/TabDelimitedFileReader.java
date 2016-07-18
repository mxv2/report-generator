package ru.mkiryanov.report.io.reader;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * User: maxim-kiryanov
 * Time: 17.07.16 23:39
 */
public class TabDelimitedFileReader implements Reader {
	private String charsetName;
	private List<String[]> lines;

	public TabDelimitedFileReader(String charsetName) {
		this.charsetName = charsetName;
	}

	@Override
	public void read(InputStream in) {
		lines = new LinkedList<>();

		Scanner scanner = new Scanner(in, charsetName);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			lines.add(line.split("\t"));
		}
	}

	@Override
	public List<String[]> getLines() {
		return lines;
	}
}
