package ru.mkiryanov.report.generator.formatter;

import java.util.List;

/**
 * User: maxim-kiryanov
 * Time: 17.07.16 17:17
 */
public interface TextFormatter {
	String format(String text);
	List<String> formatToStringList(String text);
}
