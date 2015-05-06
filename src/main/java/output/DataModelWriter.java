package output;

import model.data.DataColumn;
import model.data.DataModel;
import model.data.DataRow;
import model.reader.DataReader;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Write output after analysis is done
 */
public class DataModelWriter {
	public static void write(DataModel datamodel, String filename, String delimiter){
		Iterator rows = datamodel.getRows();
		Map<String, DataColumn> columnsData = datamodel.getColumns();
		List<DataColumn> columnList = new ArrayList<>();

		for (Object o : columnsData.entrySet()) {
			Map.Entry pair = (Map.Entry) o;
			DataColumn column = (DataColumn) pair.getValue();
			columnList.add(column);
		}

		try (PrintWriter writer = new PrintWriter(filename,"UTF-8")){

			while(rows.hasNext()){
				DataRow row = (DataRow) rows.next();
				for(DataColumn col: columnList) {
					writer.print(row.getValue(col).toString() + delimiter);
				}
				writer.println();
			}
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}



	public static void main(String[] args) {
		File file = new File("res/ADMIRE 2.txt");
		DataReader dataReader = new DataReader();
		System.out.println("datareader made");
		try {
			DataModel dataModel = dataReader.readData(file);
			System.out.println("data read");
			write(dataModel, "sample.txt", "\t");
			System.out.println("data written");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}


