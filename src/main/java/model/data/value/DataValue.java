package model.data.value;


/**
 * Abstract class DataValue describing the DataValue objects.
 * 
 * @param <Type>
 *            return type of getValue()
 */

public abstract class DataValue<Type> implements Comparable<DataValue> {
	private boolean isNull;

	public abstract Type getValue();

	public abstract String toString();

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DataValue)) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		if (isNull || ((DataValue) obj).isNull()) {
			return false;
		}
		return doEquals(obj);
	}

	@Override
	public int hashCode() {
		return doHashCode();
	}
	
	public abstract int doHashCode();

	/**
	 * Shows true if the value of the object is Null.
	 *
	 * @return isNull
	 */
	public boolean isNull() {
		return isNull;
	}

	/**
	 * Set true if value of the object is null.
	 *
	 * @param val false if value != null
	 */
	protected void setNull(boolean val) {
		isNull = val;
	}

	/**
	 * Additional function for equals for specific characteristics of each
	 * DataValue.
	 *
	 * @param obj The object to compare
	 * @return true if object value equals this value (null != null)
	 */
	protected abstract boolean doEquals(Object obj);

	/**
	 * Return a null instance of the datavalue passed in the argument.
	 * @param classType type of the null datavalue.
	 * @return null instance of the classType
	 */
	public static DataValue getNullInstance(Class<? extends DataValue> classType) {
		try {
			return classType.getConstructor(
					classType.getMethod("getValue").getReturnType())
					.newInstance(new Object[1]);
		} catch (Exception e) {
			throw new IllegalArgumentException("no such class");
		}
	}

}
