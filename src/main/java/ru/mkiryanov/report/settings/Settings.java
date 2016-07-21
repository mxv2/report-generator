package ru.mkiryanov.report.settings;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * User: maxim-kiryanov
 * Time: 20.07.16 23:26
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Settings {
	private Page page;
	@XmlElementWrapper(name = "columns")
	@XmlElement(name = "column")
	private List<Column> columns;

	public Settings() {
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	public static Settings read(String fileName) throws JAXBException {
		File file = new File(fileName);
		JAXBContext jaxbContext = JAXBContext.newInstance(Settings.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		return (Settings) jaxbUnmarshaller.unmarshal(file);
	}

	public static class Page {
		private int width;
		private int height;

		public Page() {
		}

		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}

		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}
	}

	public static class Column {
		private String title;
		private int width;

		public Column() {
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}
	}
}
