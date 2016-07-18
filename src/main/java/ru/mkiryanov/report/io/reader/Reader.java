package ru.mkiryanov.report.io.reader;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

/**
 * User: maxim-kiryanov
 * Time: 17.07.16 23:29
 */
public interface Reader {
	void read(InputStream in);
	List<String[]> getLines();
}
