package ru.mkiryanov.report.model;

/**
 * User: maxim-kiryanov
 * Time: 18.07.16 22:37
 */
public class Column {
	private Integer id;
	private String title;
	private int width;

	public Column(int id, String title, int width) {
		this.id = id;
		this.title = title;
		this.width = width;
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public int getWidth() {
		return width;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof Column) && id.equals(((Column) obj).getId());
	}
}
