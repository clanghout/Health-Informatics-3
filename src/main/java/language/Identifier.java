package language;

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

	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
