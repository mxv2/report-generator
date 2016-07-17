package ru.mkiryanov.report.generator.formatter;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * User: maxim-kiryanov
 * Time: 16.07.16 10:39
 */
@RunWith(Parameterized.class)
public class TextBoundaryFormatterTest {
	private static final String DEFAULT_STRING = "TestString";
	private static final int DEFAULT_STRING_LENGTH = "TestString".length();

	private String originString;
	private Integer width;
	private String expectedString;
	private TextBoundaryFormatter formatter;

	@Parameterized.Parameters
	public static Collection<Object[]> getParameters() {
		return Arrays.asList(new Object[][] {
			{ DEFAULT_STRING, DEFAULT_STRING_LENGTH, DEFAULT_STRING },
			{ DEFAULT_STRING, DEFAULT_STRING_LENGTH - 5, DEFAULT_STRING.substring(0, DEFAULT_STRING_LENGTH - 5) },
			{ DEFAULT_STRING, DEFAULT_STRING_LENGTH + 5, DEFAULT_STRING + "     " }
		});
	}

	public TextBoundaryFormatterTest(String originString, Integer width, String expectedString) {
		this.originString = originString;
		this.width = width;
		this.expectedString = expectedString;

		formatter = new TextBoundaryFormatter(width);
	}

	@Test
	public void testFormatString() {
		String formattedString = formatter.format(originString);

		assertThat("Invalid result for width " + width, formattedString, is(expectedString));
	}
}