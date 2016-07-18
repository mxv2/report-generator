package ru.mkiryanov.report.io.reader;

import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * User: maxim-kiryanov
 * Time: 18.07.16 21:11
 */
public class TabDelimitedFileReaderTest {
	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void testReadSimpleUTF8File() {
		List<String[]> lines = readLines("/data/test-utf8.tsv", "UTF-8");

		assertThat("Should have one element", lines, hasSize(1));
		assertThat("Invalid parsing", lines.get(0), is(new String[] {"1", "11/1992", "qwerty"}));
	}

	@Test
	public void testReadUTF16File() {
		List<String[]> lines = readLines("/data/test-utf16.tsv", "UTF-16");

		assertThat("Should have one element", lines, hasSize(2));
		assertThat("Invalid parsing", lines.get(0), is(new String[] {"1", "11/1992", "qwerty"}));
		assertThat("Invalid parsing", lines.get(1), is(new String[] {"2", "42/42", "тестовая строка"}));
	}

	private List<String[]> readLines(String fileName, String charsetName) {
		InputStream in = this.getClass().getResourceAsStream(fileName);
		Reader reader = new TabDelimitedFileReader(charsetName);
		reader.read(in);

		return reader.getLines();
	}
}