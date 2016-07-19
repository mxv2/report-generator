package ru.mkiryanov.report.model;


import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * User: maxim-kiryanov
 * Time: 17.07.16 23:44
 */
public class Report {
	private Table<Long, Column, String> table = HashBasedTable.create();

	public void setCellString(long rowIndex, Column column, String s) {
		table.put(rowIndex, column, s);
	}

	public String getCellString(long rowIndex, Column column) {
		return table.get(rowIndex, column);
	}

	public long getRowsCount() {
		return table.rowKeySet().size();
	}

	public Map<Column, String> getRow(long i) {
		return table.row(i);
	}
}
