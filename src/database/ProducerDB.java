package database;

import documents.MonthlyReport;
import entities.Distributor;
import entities.Entity;
import entities.Producer;

import java.util.ArrayList;
import java.util.Observable;
import java.util.stream.Collectors;

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
            producers.forEach(p -> {
                // Get the ids of all his current distributors
                ArrayList<Long> getIds = p.getAssignedDistributors().stream()
                        .map(Entity::getId).collect(Collectors.toCollection(ArrayList::new));

                // Create the report
                MonthlyReport newReport = new MonthlyReport(turn, getIds);
                p.getReports().add(newReport);
            });
        }
    }

    /**
     * Method that adds all the distributors from simulation as
     * observers to the producers' database.
     *
     * @param distributors information about all distributors
     */
    public void addAllObservers(final ArrayList<Distributor> distributors) {
        distributors.forEach(this::addObserver);
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
