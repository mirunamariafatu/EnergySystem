package database;

import documents.MonthlyReport;
import entities.Distributor;
import entities.Producer;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Information about all producers and methods of processing their data, retrieved
 * from parsing the input files.
 */
@SuppressWarnings("deprecation")
public final class ProducerDB extends Observable {
    /**
     * Information about producers, saved from input.
     */
    private final ArrayList<Producer> producers;

    public ProducerDB(ArrayList<Producer> producers) {
        this.producers = producers;
    }

    public ArrayList<Producer> getProducers() {
        return producers;
    }

    /**
     * Method through all the producers write their monthly stats
     * regarding their current assigned distributors.
     *
     * @param turn current month
     */
    public void writeMonthlyReports(final int turn) {
        if (turn != 0) {
            for (Producer p : producers) {
                ArrayList<Long> getIds = new ArrayList<>();

                // Get the ids of all his current distributors
                for (Distributor d : p.getAssignedDistributors()) {
                    getIds.add(d.getId());
                }
                // Create the report
                MonthlyReport newReport = new MonthlyReport(turn, getIds);
                p.getReports().add(newReport);
            }
        }
    }

    /**
     * Method that adds all the distributors from simulation as
     * observers to the producers' database.
     *
     * @param distributors information about all distributors
     */
    public void addAllObservers(final ArrayList<Distributor> distributors) {
        for (Distributor d : distributors) {
            addObserver(d);
        }
    }

    /**
     * Method through all the observers of producers' database
     * get notified.
     *
     * @param producer current producer that has updated his information
     */
    public void newChangesApplied(final Producer producer) {
        setChanged();
        notifyObservers(producer);
    }
}
