package documents;

import entities.Consumer;

/**
 * Class that contains general information about a contract and
 * methods of processing its data.
 */
public final class Contract {
    /**
     * The owner of the contract
     */
    private final Consumer consumer;
    /**
     * The owner's id
     */
    private long consumerId;
    /**
     * The declared price at the signing of the contract
     */
    private long price;
    /**
     * The remaining payment months
     */
    private long remainedContractMonths;

    public Contract(final long consumerId, final long price, final long remainedContractMonths,
                    final Consumer consumer) {
        this.consumerId = consumerId;
        this.price = price;
        this.remainedContractMonths = remainedContractMonths;
        this.consumer = consumer;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(final long price) {
        this.price = price;
    }

    public long getRemainedContractMonths() {
        return remainedContractMonths;
    }

    public void setRemainedContractMonths(final long remainedContractMonths) {
        this.remainedContractMonths = remainedContractMonths;
    }

    public long getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(final long consumerId) {
        this.consumerId = consumerId;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    /**
     * Method that updates the remaining payment months.
     */
    public void contractUpdate() {
        remainedContractMonths -= 1;
        if (remainedContractMonths == 0) {
            this.consumer.setCurrentContract(null);
        }
    }
}
