package database;

import entities.Distributor;
import entities.Producer;

import java.util.ArrayList;

/**
 * Information about all distributors and methods of processing their data, retrieved
 * from parsing the input files
 */
public final class DistributorDB {
    /**
     * Information about distributors, saved from input
     */
    private final ArrayList<Distributor> distributors;

    public DistributorDB(final ArrayList<Distributor> distributors) {
        this.distributors = distributors;
    }

    public ArrayList<Distributor> getDistributors() {
        return distributors;
    }

    /**
     * Method in which all distributors pay their monthly fees.
     */
    public void payAllTaxes() {
        distributors.stream().filter(d -> !d.getIsBankrupt()).forEach(Distributor::payTaxes);
    }

    /**
     * Method in which distributors choose new producers, using their
     * particular strategy.
     *
     * @param producers producers' database
     */
    public void applyStrategyProducer(final ArrayList<Producer> producers) {
        for (Distributor d : distributors) {
            // Check if the distributor must choose new producers
            if (d.getHasToReapplyStrategy()) {

                // Check if distributor has current producers
                if (!d.getCurrentProducers().isEmpty()) {

                    // Remove the distributor from all its producers
                    d.getCurrentProducers().forEach(p -> p.getAssignedDistributors().remove(d));

                    // Remove all current producers
                    d.getCurrentProducers().clear();
                }
                // Distributor selects new producers
                d.getStrategy().chooseProducers(d, producers);
                // Reset the flag
                d.setHasToReapplyStrategy(false);
            }
        }
    }
}
