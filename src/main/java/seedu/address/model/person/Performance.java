package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Person's remark in the address book.
 * Guarantees: immutable; is always valid
 */
public class Performance {
    public final String value;
    public static final String NULL_PERFORMANCE = "None yet!";

    /**
     * Accepts remark that is not empty
     * @param performance String of a performance
     */
    public Performance(String performance) {
        requireNonNull(performance);
        //Add check to make sure it is not a gibberish number
        value = performance;
    }

    /**
     * Initiates null performance object without causing error.
     */
    public Performance() {
        value = NULL_PERFORMANCE;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Performance // instanceof handles nulls
                && value == ((Performance) other).value); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
