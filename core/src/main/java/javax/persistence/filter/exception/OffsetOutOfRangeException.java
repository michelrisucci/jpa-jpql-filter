package javax.persistence.filter.exception;

/**
 * @author Michel Risucci
 */
public class OffsetOutOfRangeException extends RuntimeException {

	private static final long serialVersionUID = -1784941482927697043L;
	private static final String MESSAGE_PATTERN = "Offset value %d is out of pagination range: found only %d results.";

	private long count;
	private int offset;

	/**
	 * 
	 */
	public OffsetOutOfRangeException(long count, int offset) {
		super(String.format(MESSAGE_PATTERN, offset, count));
		this.count = count;
		this.offset = offset;
	}

	/**
	 * @return
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @param offset
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * @return
	 */
	public long getCount() {
		return count;
	}

	/**
	 * @param count
	 */
	public void setCount(long count) {
		this.count = count;
	}

}