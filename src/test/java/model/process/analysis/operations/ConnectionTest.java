package model.process.analysis.operations;

import com.oracle.webservices.internal.api.databinding.Databinding;
import model.data.DataModel;
import model.data.DataTable;
import model.data.DataTableBuilder;
import model.data.value.DateTimeValue;
import model.data.value.StringValue;
import model.data.value.TimeValue;
import model.language.ColumnIdentifier;
import model.language.Identifier;
import org.jfree.data.xy.XYDatasetTableModel;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jens on 6/10/15.
 */
public class ConnectionTest {
	DataModel model;
	DataTable table1;
	DataTable table2;
	Identifier t1;
	Identifier t2;
	ColumnIdentifier c1;
	ColumnIdentifier c2;

	@Before
	public void setup() {
		model = new DataModel();
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("table1");
		builder.createColumn("time", TimeValue.class);
		builder.createColumn("value", StringValue.class);
		builder.createRow(new TimeValue(1,1,2), new StringValue("1"));
		builder.createRow(new TimeValue(5,2,2), new StringValue("5"));
		builder.createRow(new TimeValue(4,1,2), new StringValue("3"));
		builder.createRow(new TimeValue(6,1,2), new StringValue("6"));
		table1 = builder.build();

		builder = new DataTableBuilder();
		builder.setName("table2");
		builder.createColumn("time", TimeValue.class);
		builder.createColumn("value2", StringValue.class);
		builder.createRow(new TimeValue(4,1,4), new StringValue("4"));
		builder.createRow(new TimeValue(3,1,2), new StringValue("2"));
		builder.createRow(new TimeValue(7,1,2), new StringValue("7"));
		table2 = builder.build();

		t1 = new Identifier("table1");
		t2 = new Identifier("table2");
		c1 = new ColumnIdentifier(t1, new Identifier("time"));
		c2 = new ColumnIdentifier(t2, new Identifier("time"));
		model.add(table1);
		model.add(table2);
	}

	@Test
	public void testDoProcess() throws Exception {
		DataTableBuilder builder = new DataTableBuilder();
		builder.setName("res");
		builder.createColumn("time", TimeValue.class);
		builder.createColumn("value", StringValue.class);
		builder.createColumn("value2", StringValue.class);

		builder.createRow(new TimeValue(1,1,2), new StringValue("1"), new StringValue(null));
		builder.createRow(new TimeValue(3,1,2), new StringValue(null), new StringValue("2"));
		builder.createRow(new TimeValue(4,1,2), new StringValue("3"), new StringValue(null));
		builder.createRow(new TimeValue(4,1,4), new StringValue(null), new StringValue("4"));
		builder.createRow(new TimeValue(5,2,2), new StringValue("5"), new StringValue(null));
		builder.createRow(new TimeValue(6,1,2), new StringValue("6"), new StringValue(null));
		builder.createRow(new TimeValue(7,1,2), new StringValue(null), new StringValue("7"));

		DataTable expected = builder.build();
		Connection con = new Connection("res", t1, c1, t2, c2);
		con.setDataModel(model);
		DataTable res = (DataTable) con.process();

		assertTrue(expected.equalsSoft(res));

	}
}