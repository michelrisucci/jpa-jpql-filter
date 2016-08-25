package javax.persistence.filter.core;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

import javax.persistence.filter.Filter;
import javax.persistence.filter.core.conditional.exact.Exact;

/**
 * TODO Temporary Fix.
 * 
 * @author Michel Risucci
 */
public class PassthroughRawClause extends Exact {

	private String rawJpql;

	public PassthroughRawClause(String rawJpql, Operation operation, Object value) {
		super("", operation, value);

		this.rawJpql = rawJpql;
		this.queryParamName = "_" + randomAlphabetic(1) + randomAlphanumeric(15);
	}

	@Override
	protected String getJpqlClause() {
		String clause = rawJpql + " " + operation.getOperand() + " :" + queryParamName + " ";
		if (allowNulls) {
			return "(" + clause + "OR " + clause + " IS NULL) ";
		} else {
			return clause;
		}
	}

	public void postProcess(Filter<?> filter) {
		// disabled
	}

}
