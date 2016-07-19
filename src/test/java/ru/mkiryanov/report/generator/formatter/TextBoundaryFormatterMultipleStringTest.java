package ru.mkiryanov.report.generator.formatter;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * User: maxim-kiryanov
 * Time: 17.07.16 20:25
 */
@RunWith(Parameterized.class)
public class TextBoundaryFormatterMultipleStringTest {
	private String originString;
	private Integer width;
	private List<String> expectedStrings;
	private TextBoundaryFormatter formatter;

	@Parameterized.Parameters
	public static Collection<Object[]> getParameters() {
		return Arrays.asList(new Object[][] {
				{ "Павлов Дмитрий", 7, Arrays.asList("Павлов ", "Дмитрий") },
				{ "Павлов Константин", 7, Arrays.asList("Павлов ", "Констан", "тин    ") },
				{ "Н/Д", 7, Arrays.asList("Н/Д    ") },
				{ "Ким Чен Ир", 7, Arrays.asList("Ким Чен", "Ир     ") },
				{ "Юлианна-Оксана Сухово-Кобылина", 7, Arrays.asList("Юлианна", "-Оксана", "Сухово-", "Кобылин", "а      ")}
		});
	}

	public TextBoundaryFormatterMultipleStringTest(String originString, Integer width, List<String> expectedStrings) {
		this.originString = originString;
		this.width = width;
		this.expectedStrings = expectedStrings;

		formatter = new TextBoundaryFormatter(width);
	}

	@Test
	public void testFormatString() {
		List<String> formattedStrings = formatter.formatToStringList(originString);

		assertThat("Invalid result for string \"" + originString + "\"", formattedStrings, is(expectedStrings));
	}

//	private Matcher<Iterator<String>> hasItems(final Collection<String> collection) {
//		return new BaseMatcher<Iterator<String>>() {
//			@Override
//			public boolean matches(Object o) {
//				Iterator<String> actualIterator = (Iterator<String>) o;
//				Iterator<String> expectedIterator = collection.iterator();
//
//				while (actualIterator.hasNext() && expectedIterator.hasNext()) {
//					String actual = actualIterator.next();
//					String expected = expectedIterator.next();
//					if (!actual.equals(expected)) {
//						return false;
//					}
//				}
//
//				if (actualIterator.hasNext() || expectedIterator.hasNext()) {
//					return false;
//				}
//
//				return true;
//			}
//
//			@Override
//			public void describeTo(Description description) {
//
//			}
//		};
//	}
}
