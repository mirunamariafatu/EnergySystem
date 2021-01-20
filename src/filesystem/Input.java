package filesystem;

import database.ProducerDB;
import entities.Consumer;
import entities.Distributor;
import entities.EntityFactory;
import entities.Producer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import utils.Constants;

import java.util.ArrayList;

/**
 * The class contains information about input.
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
     * Method that gets and sets the new distributors' costs update every month.
     *
     * @param turn         current month
     * @param distributors information about all distributors
     */
    public void setDistributorsMonthlyUpdate(final int turn,
                                             final ArrayList<Distributor> distributors) {
        if (turn != 0) {
            JSONObject currentMonth = (JSONObject) monthlyUpdates.get(turn - 1);
            JSONArray costChanges = (JSONArray) currentMonth.get(Constants.DISTRIBUTOR_CHANGES);
            if (costChanges.isEmpty()) {
                return;
            }
            for (Object o : costChanges) {
                long id = (long) ((JSONObject) o).get(Constants.ID);
                long newInfrastructureCost =
                        (long) ((JSONObject) o).get(Constants.INFRASTRUCTURE_COST);

                // Set the new updates to the targeted distributor
                distributors.get((int) id).getDistributorUpdates(newInfrastructureCost);
            }
        }
    }

    /**
     * Method that gets and sets the new producers' changes every month.
     *
     * @param turn          current month
     * @param producersData producers' database
     */
    public void setProducersMonthlyUpdate(final int turn, ProducerDB producersData) {
        if (turn != 0) {
            JSONObject currentMonth = (JSONObject) monthlyUpdates.get(turn - 1);
            JSONArray quantityChanges = (JSONArray) currentMonth.get(Constants.PRODUCER_CHANGES);
            if (quantityChanges.isEmpty()) {
                return;
            }
            for (Object o : quantityChanges) {
                long id = (long) ((JSONObject) o).get(Constants.ID);
                long newQuantity =
                        (long) ((JSONObject) o).get(Constants.ENERGY_PER_DISTRIBUTOR);
                Producer changedProducer = producersData.getProducers().get((int) id);

                // Set the new updates to the targeted producer
                changedProducer.getProducerUpdates(newQuantity);
                // Notify all observers that there are new updates
                producersData.newChangesApplied(changedProducer);
            }
        }
    }

    /**
     * Method that gets all the new consumers from monthly updates.
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
     * Method that processes the monthly update.
     */
    public void getUpdates(final int turn, final ArrayList<Consumer> consumers,
                           final ArrayList<Distributor> distributors) {
        setDistributorsMonthlyUpdate(turn, distributors);
        getNewConsumers(turn, consumers);
    }

    /**
     * The method reads the consumers from input file.
     *
     * @return an ArrayList of consumers
     */
    public ArrayList<Consumer> setConsumersData() {
        JSONArray consumersArray = (JSONArray) initialData.get(Constants.CONSUMERS);

        ArrayList<Consumer> consumers = new ArrayList<>();
        EntityFactory factory = EntityFactory.getInstance();

        // Create the entity
        for (Object o : consumersArray) {
            Consumer newConsumer = (Consumer) factory.createEntity(Constants.CONSUMER,
                    (JSONObject) o);
            consumers.add(newConsumer);
        }
        return consumers;
    }

    /**
     * The method reads the distributors from input file.
     *
     * @return an ArrayList of distributors
     */
    public ArrayList<Distributor> setDistributorsData() {
        JSONArray distributorsArray = (JSONArray) initialData.get(Constants.DISTRIBUTORS);

        ArrayList<Distributor> distributors = new ArrayList<>();
        EntityFactory factory = EntityFactory.getInstance();

        // Create the entity
        for (Object o : distributorsArray) {
            Distributor newDistributor =
                    (Distributor) factory.createEntity(Constants.DISTRIBUTOR, (JSONObject) o);
            distributors.add(newDistributor);
        }
        return distributors;
    }

    /**
     * The method reads the producers from input file.
     *
     * @return an ArrayList of producers
     */
    public ArrayList<Producer> setProducersData() {
        JSONArray producersArray = (JSONArray) initialData.get(Constants.PRODUCERS);

        ArrayList<Producer> producers = new ArrayList<>();
        EntityFactory factory = EntityFactory.getInstance();

        // Create the entity
        for (Object o : producersArray) {
            Producer newProducer =
                    (Producer) factory.createEntity(Constants.PRODUCER, (JSONObject) o);
            producers.add(newProducer);
        }
        return producers;
    }

}
