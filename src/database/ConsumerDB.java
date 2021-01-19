package database;

import entities.Consumer;
import entities.Distributor;

import java.util.ArrayList;

/**
 * Information about all consumers and methods of processing their data, retrieved
 * from parsing the input files
 */
public final class ConsumerDB {
    /**
     * Information about consumers, saved from input
     */
    private final ArrayList<Consumer> consumers;

    public ConsumerDB(final ArrayList<Consumer> consumers) {
        this.consumers = consumers;
    }

    public ArrayList<Consumer> getConsumers() {
        return consumers;
    }

    /**
     * Method in which all consumers who no longer have an active contract
     * are looking for a new distributor
     *
     * @param distributors information about all distributors
     */
    public void searchForDistributors(final ArrayList<Distributor> distributors) {
        // Update the distributors' prices
        for (Distributor d : distributors) {
            d.calculatePrice();
        }
        for (Consumer c : consumers) {
            if ((c.getCurrentContract() == null) && (!c.getIsBankrupt())) {
                // Get a new distributor
                Distributor currentDistributor = c.chooseDistributor(distributors);
                assert currentDistributor != null;
                // Sign a new contract
                c.signContract(currentDistributor);
            }
        }
    }

    /**
     * Method in which consumers collect their monthly salaries
     */
    public void collectConsumersSalary() {
        for (Consumer c : consumers) {
            if (!c.getIsBankrupt()) {
                c.getSalary();
            }
        }
    }

    /**
     * Method in which consumers pay their current contract price
     */
    public void payAllPrices() {
        for (Consumer c : consumers) {
            if (!c.getIsBankrupt()) {
                c.payPrice();
            }
        }
    }
}
