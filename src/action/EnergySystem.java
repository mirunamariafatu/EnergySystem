package action;

import database.ConsumerDB;
import database.DistributorDB;
import database.ProducerDB;
import filesystem.Input;

/**
 * The class simulates a real energy system with
 * all its functionalities.
 */
public final class EnergySystem {

    private static EnergySystem instance = null;

    private EnergySystem() {
    }

    /**
     * Gets the instance.
     *
     * @return the instance
     */
    public static EnergySystem getInstance() {
        if (instance == null) {
            instance = new EnergySystem();
        }

        return instance;
    }

    /**
     * Method that launches the energy system simulation.
     *
     * @param consumersData    information about consumers
     * @param distributorsData information about distributors
     * @param input            information about input data
     */
    public void energySystemSimulation(final ConsumerDB consumersData,
                                       final DistributorDB distributorsData,
                                       final ProducerDB producersData, final Input input) {

        // Add all distributors as observers to the producers' database
        producersData.addAllObservers(distributorsData.getDistributors());

        for (int i = 0; i < input.getNumberOfTurns() + 1; i++) {

            // Get monthly updates
            input.getUpdates(i, consumersData.getConsumers(),
                    distributorsData.getDistributors());
            // Distributors select their producers
            distributorsData.applyStrategyProducer(producersData.getProducers());
            // Consumers receive their salaries
            consumersData.collectConsumersSalary();
            // Consumers without a valid contract are searching for a new one
            consumersData.searchForDistributors(distributorsData.getDistributors());
            // Consumers pay their current contract
            consumersData.payAllPrices();
            // Distributors pay their monthly fees
            distributorsData.payAllTaxes();
            // Get updates from producers and notify all distributors assigned to them
            input.setProducersMonthlyUpdate(i, producersData);
            // Distributors choose their producers only if new updates appear
            distributorsData.applyStrategyProducer(producersData.getProducers());
            // Producers write their monthly balance sheet
            producersData.writeMonthlyReports(i);
        }
    }
}
