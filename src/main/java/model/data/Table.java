package model.data;

import java.util.Iterator;

/**
 * Created by jens on 5/19/15.
 */
public interface Table {
	Iterator<? extends Row> iterator();

	void flagNotDelete(Row row);
	void deleteNotFlagged();
}
