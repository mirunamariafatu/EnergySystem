package action;

import database.ConsumerDB;
import database.DistributorDB;
import filesystem.Input;

/**
 * The class simulates a real energy system with
 * all its functionalities
 */
public final class EnergySystem {

    private static EnergySystem instance = null;

    private EnergySystem() {
    }

    /**
     * Gets the instance
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
     * Method that launches the energy system simulation
     *
     * @param consumersData    information about consumers
     * @param distributorsData information about distributors
     * @param input            information about input data
     */
    public void energySystemSimulation(final ConsumerDB consumersData,
                                       final DistributorDB distributorsData,
                                       final Input input) {

        for (int i = 0; i < input.getNumberOfTurns() + 1; i++) {
            // Get monthly updates
            input.getUpdates(i, consumersData.getConsumers(), distributorsData.getDistributors());
            // Consumers receive their salaries
            consumersData.collectConsumersSalary();
            // Consumers without a valid contract are searching for a new one
            consumersData.searchForDistributors(distributorsData.getDistributors());
            // Consumers pay their current contract
            consumersData.payAllPrices();
            // Distributors pay their monthly fees
            distributorsData.payAllTaxes();
        }
    }
}
