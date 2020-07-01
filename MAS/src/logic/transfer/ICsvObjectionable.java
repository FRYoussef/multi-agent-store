package logic.transfer;

public interface ICsvObjectionable {

    /**
     * For hash collections
     * @return id in the DB
     */
    @Override
    int hashCode();

    /**
     * String as a entry line in the CSV DB
     * @return csv row
     */
    String toCsvFormat();
}
