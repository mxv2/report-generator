package ru.mkiryanov.report.settings;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;

/**
 * User: maxim-kiryanov
 * Time: 20.07.16 23:37
 */
public class SettingsTest {
	@Test
	public void read() throws Exception {
		String fileName = SettingsTest.class.getResource("/settings.xml").getFile();

		Settings settings = Settings.read(fileName);
		assertThat(settings, notNullValue());

		Settings.Page page = settings.getPage();
		assertThat(page.getHeight(), is(12));
		assertThat(page.getWidth(), is(32));

		List<Settings.Column> columns = settings.getColumns();
		assertColumn(columns.get(0), "Номер", 8);
		assertColumn(columns.get(1), "Дата", 7);
		assertColumn(columns.get(2), "ФИО", 7);
	}

	private void assertColumn(Settings.Column column, String title, int width) {
		assertThat("Invalid title", column.getTitle(), is(title));
		assertThat("Invalid width", column.getWidth(), is(width));
	}
}