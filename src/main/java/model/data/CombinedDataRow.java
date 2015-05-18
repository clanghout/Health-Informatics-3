package model.data;

import java.util.Arrays;

/**
 * Created by jens on 5/18/15.
 */
public class CombinedDataRow {
	private DataRow row;
	private CombinedDataRow rows;

	public CombinedDataRow(DataRow row, CombinedDataRow rows) {
		this.row = row;
		this.rows = rows;
	}
}
