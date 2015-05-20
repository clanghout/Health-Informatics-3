package language;

/**
 * A name for a value.
 *
 * Created by Boudewijn on 20-5-2015.
 * @param <T> The type of object this Identifier is referring to.
 */
public class Identifier<T> {

	private String name;

	public Identifier(String name) {
		this.name = name;
	}

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
