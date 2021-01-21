package database;

import entities.Consumer;
import entities.Distributor;

import java.util.ArrayList;

/**
 * Information about all consumers and methods of processing their data, retrieved
 * from parsing the input files.
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
     * are looking for a new distributor.
     *
     * @param distributors information about all distributors
     */
    public void searchForDistributors(final ArrayList<Distributor> distributors) {
        // Update the distributors' prices and production costs
        distributors.forEach(d -> {
            d.calculateProductionCost();
            d.calculatePrice();
        });

        consumers.stream().filter(c -> (c.getCurrentContract() == null) && (!c.getIsBankrupt()))
                .forEach(c -> {
                    // Get a new distributor
                    Distributor currentDistributor = c.chooseDistributor(distributors);
                    // Sign a new contract
                    assert currentDistributor != null;
                    c.signContract(currentDistributor);
                });
    }

    /**
     * Method in which consumers collect their monthly salaries.
     */
    public void collectConsumersSalary() {
        consumers.stream().filter(c -> !c.getIsBankrupt()).forEach(Consumer::getSalary);
    }

    /**
     * Method in which consumers pay their current contract price.
     */
    public void payAllPrices() {
        consumers.stream().filter(c -> !c.getIsBankrupt()).forEach(Consumer::payPrice);
    }
}
