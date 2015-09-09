package javax.persistence.filter.core.conditional.exact;

import javax.persistence.TypedQuery;
import javax.persistence.filter.core.Where;

/**
 * @author Michel Risucci
 */
public abstract class Exact extends Where {

	private Operation operation;

	/**
	 * @param path
	 * @param operation
	 * @param value
	 */
	protected Exact(String path, Operation operation, Object value) {
		super(path, value);
		this.operation = operation;
	}

	/**
	 * @return
	 */
	public Operation getOperation() {
		return operation;
	}

	@Override
	protected String getClause() {
		return getRealPath() + " " + operation.getOperand() + " :" + queryParamName + " ";
	}

	@Override
	public <E> TypedQuery<E> compileClause(TypedQuery<E> query) {
		return query.setParameter(queryParamName, values[0]);
	}

	/**
	 * @author Michel Risucci
	 */
	protected enum Operation {
		EQUAL("="), //
		NOT_EQUAL("<>"), //
		LESSER_THAN("<"), //
		LESSER_THAN_OR_EQUAL("<="), //
		GREATER_THAN(">"), //
		GREATER_THAN_OR_EQUAL(">=");

		private String operand;

		/**
		 * @param operand
		 */
		private Operation(String operand) {
			this.operand = operand;
		}

		/**
		 * @return
		 */
		public String getOperand() {
			return operand;
		}
	}

}