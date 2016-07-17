package ru.mkiryanov.report.generator.formatter;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

/**
 * User: maxim-kiryanov
 * Time: 16.07.16 10:32
 */
public class TextBoundaryFormatter extends AbstractTextFormatter {
	private int maxWidth;

	public TextBoundaryFormatter(int maxWidth) {
		this.maxWidth = maxWidth;
	}

	@Override
	public String format(String text) {
		return formatToStringIterator(text).next();
	}

	@Override
	public Iterator<String> formatToStringIterator(String text) {
		StringBuilder formattingBuilder = format(text, maxWidth);

		List<String> list = new ArrayList<>();
		for (int i = 0; i < formattingBuilder.length(); i += maxWidth) {
			list.add(formattingBuilder.substring(i, i + maxWidth));
		}

		System.out.println(list);
		return list.iterator();
	}

	private StringBuilder format(String text, int offset) {
		StringBuilder formattingBuilder = new StringBuilder(text);
		Matcher matcher = Pattern.compile("[^\\d\\w]").matcher(formattingBuilder);

		int pointer = 0;
		for (; pointer < formattingBuilder.length(); ) {
			eraseBeginningSpaces(formattingBuilder, pointer);

			int start = pointer;
			while (start < formattingBuilder.length() && matcher.find(start)) {
				int internalOffset = matcher.end() - pointer;
				if (internalOffset == offset) {
					break;
				} else if (internalOffset < offset) {
					start = matcher.end();
				} else {
					int wordLength = matcher.end() - start;
					int lengthToBound = pointer + offset - start;

					if (lengthToBound >= wordLength / 2) {
						break;
					} else {
						if (wordLength <= offset) {
							// add spaces to bound
							addEndingSpaces(formattingBuilder, start, pointer + offset);
						} else {
							break;
						}
					}
				}
			}

			pointer += offset;
		}

		appendEndingSpaces(formattingBuilder, pointer - formattingBuilder.length());

		return formattingBuilder;
	}

	private void eraseBeginningSpaces(StringBuilder stringBuilder, int start) {
		char c = stringBuilder.charAt(start);
		int end = start;

		while (Character.isSpaceChar(c)) {
			c = stringBuilder.charAt(++end);
		}

		stringBuilder.delete(start, end);
	}

	private void addEndingSpaces(StringBuilder stringBuilder, int start, int end) {
		while (++start < end) {
			stringBuilder.insert(start, ' ');
		}
	}

	private void appendEndingSpaces(StringBuilder stringBuilder, int count) {
		while (count-- > 0) {
			stringBuilder.append(' ');
		}
	}

	public static void main(String[] args) {
		System.out.println(
				IntStream.range(0, 2)
						.map(operand -> operand * 10)
						.mapToObj(Integer::valueOf)
						.collect(toList()));
	}
}