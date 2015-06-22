package model.language;

/**
 * A name for a value.
 *
 * Created by Boudewijn on 20-5-2015.
 * @param <T> The type of object this Identifier is referring to.
 */
public class Identifier<T> {

	private String name;

	/**
	 * Construct a new Identifier.
	 * @param name The name of the identifier.
	 */
	public Identifier(String name) {
		this.name = name;
	}

	/**
	 * Get the name of this identifier.
	 * @return The name of this identifier.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Check if this identifier is equal to an object.
	 * @param o The other object to check for equality.
	 * @return True if the given object is equal to this object.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Identifier<?> that = (Identifier<?>) o;

		return name.equals(that.name);
	}

	/**
	 * Calculate a hashCode for this Identifier.
	 * @return The hashCode for this Identifier.
	 */
	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public String toString() {
		return name;
	}
}
