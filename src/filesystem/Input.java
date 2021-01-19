package filesystem;

import entities.Consumer;
import entities.Distributor;
import entities.EntityFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import utils.Constants;

import java.util.ArrayList;

/**
 * The class contains information about input
 */
@SuppressWarnings("deprecation")
public final class Input {
    /**
     * Information retrieved from parsing test files
     */
    private final long numberOfTurns;
    private final JSONObject initialData;
    private final JSONArray monthlyUpdates;

    public Input(final long numberOfTurns, final JSONObject initialData,
                 final JSONArray monthlyUpdates) {
        this.numberOfTurns = numberOfTurns;
        this.initialData = initialData;
        this.monthlyUpdates = monthlyUpdates;
    }

    public JSONArray getMonthlyUpdates() {
        return monthlyUpdates;
    }

    public long getNumberOfTurns() {
        return numberOfTurns;
    }

    /**
     * Method that gets and sets the new costs update every month
     *
     * @param turn         current month
     * @param distributors information about all distributors
     */
    public void setCostsMonthlyUpdate(final int turn, final ArrayList<Distributor> distributors) {
        if (turn != 0) {
            JSONObject currentMonth = (JSONObject) monthlyUpdates.get(turn - 1);
            JSONArray costChanges = (JSONArray) currentMonth.get(Constants.COSTS_CHANGES);
            if (costChanges.isEmpty()) {
                return;
            }
            for (Object o : costChanges) {
                long id = (long) ((JSONObject) o).get("id");
                long newInfrastructureCost =
                        (long) ((JSONObject) o).get(Constants.INFRASTRUCTURE_COST);
                long newProductionCost = (long) ((JSONObject) o).get(Constants.PRODUCTION_COST);

                // Set the new updates to the targeted distributor
                distributors.get((int) id).getNewUpdates(newProductionCost,
                        newInfrastructureCost);
            }
        }
    }

    /**
     * Method that gets all the new consumers from monthly updates
     *
     * @param turn      current month
     * @param consumers information about all consumers
     */
    public void getNewConsumers(final int turn, final ArrayList<Consumer> consumers) {
        if (turn != 0) {
            JSONObject currentMonth = (JSONObject) monthlyUpdates.get(turn - 1);
            JSONArray newConsumers = (JSONArray) currentMonth.get(Constants.NEW_CONSUMERS);
            EntityFactory factory = EntityFactory.getInstance();
            if (newConsumers.isEmpty()) {
                return;
            }
            for (Object o : newConsumers) {
                // Create new consumer
                Consumer newConsumer = (Consumer) factory.createEntity(Constants.CONSUMER,
                        (JSONObject) o);
                // Add the consumer to its database
                consumers.add(newConsumer);
            }
        }
    }

    /**
     * Method that processes the monthly update
     */
    public void getUpdates(final int turn, final ArrayList<Consumer> consumers,
                           final ArrayList<Distributor> distributors) {
        setCostsMonthlyUpdate(turn, distributors);
        getNewConsumers(turn, consumers);
    }

    /**
     * The method reads the consumers from input file
     *
     * @return an ArrayList of consumers
     */
    public ArrayList<Consumer> setConsumersData() {
        JSONArray consumersArray = (JSONArray) initialData.get(Constants.CONSUMERS);

        ArrayList<Consumer> consumers = new ArrayList<>();
        EntityFactory factory = EntityFactory.getInstance();

        for (Object o : consumersArray) {
            Consumer newConsumer = (Consumer) factory.createEntity(Constants.CONSUMER,
                    (JSONObject) o);
            consumers.add(newConsumer);
        }

        return consumers;
    }

    /**
     * The method reads the distributors from input file
     *
     * @return an ArrayList of distributors
     */
    public ArrayList<Distributor> setDistributorsData() {
        JSONArray distributorsArray = (JSONArray) initialData.get(Constants.DISTRIBUTORS);

        ArrayList<Distributor> distributors = new ArrayList<>();
        EntityFactory factory = EntityFactory.getInstance();

        for (Object o : distributorsArray) {
            Distributor newDistributor =
                    (Distributor) factory.createEntity(Constants.DISTRIBUTOR, (JSONObject) o);
            distributors.add(newDistributor);
        }

        return distributors;
    }
}
