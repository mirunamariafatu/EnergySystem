package database;

import entities.Distributor;

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
     * Method in which all distributors pay their monthly fees
     */
    public void payAllTaxes() {
        for (Distributor d : distributors) {
            if (!d.getIsBankrupt()) {
                d.payTaxes();
            }
        }
    }
}
