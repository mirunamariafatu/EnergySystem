package entities;

import org.json.simple.JSONObject;
import utils.Constants;

/**
 * Class that implements Factory design pattern, in order to
 * create multiple entities.
 */
@SuppressWarnings("deprecation")
public final class EntityFactory {

    private static EntityFactory instance = null;

    private EntityFactory() {
    }

    /**
     * Gets the instance.
     * @return the instance
     */
    public static EntityFactory getInstance() {
        if (instance == null) {
            instance = new EntityFactory();
        }

        return instance;
    }

    /**
     * Method that creates a custom entity.
     *
     * @param entityType the entity type
     * @param entity     JSONObject that contains information
     *                   about the entity to be created
     * @return the entity
     */
    public Entity createEntity(final String entityType, final JSONObject entity) {
        Entity newEntity = null;
        long id = (long) entity.get(Constants.ID);
        long initialBudget = (long) entity.get(Constants.INITIAL_BUDGET);

        switch (entityType) {
            case Constants.CONSUMER -> {
                long monthlyIncome = (long) entity.get(Constants.MONTHLY_INCOME);

                // Create new consumer (entity)
                newEntity = new Consumer(id, initialBudget, monthlyIncome);
            }
            case Constants.DISTRIBUTOR -> {
                long contractLength = (long) entity.get(Constants.CONTRACT_LENGTH);
                long initialInfrastructureCost =
                        (long) entity.get(Constants.INITIAL_INFRASTRUCTURE_COST);
                long initialProductionCost = (long) entity.get(Constants.INITIAL_PRODUCTION_COST);

                // Create a new distributor (entity)
                newEntity = new Distributor(id, initialBudget, contractLength,
                        initialInfrastructureCost, initialProductionCost);
            }
            default -> System.out.println(Constants.INVALID_COMMAND);
        }
        return newEntity;
    }
}
