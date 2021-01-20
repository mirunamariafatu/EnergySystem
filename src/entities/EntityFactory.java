package entities;

import org.json.simple.JSONObject;
import strategies.EnergyChoiceStrategyType;
import strategies.SelectProducersStrategy;
import strategies.SelectProducersStrategyFactory;
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
     *
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

        switch (entityType) {
            case Constants.CONSUMER -> {
                long initialBudget = (long) entity.get(Constants.INITIAL_BUDGET);
                long monthlyIncome = (long) entity.get(Constants.MONTHLY_INCOME);

                // Create new consumer (entity)
                newEntity = new Consumer(id, initialBudget, monthlyIncome);
            }
            case Constants.DISTRIBUTOR -> {
                long contractLength = (long) entity.get(Constants.CONTRACT_LENGTH);
                long initialBudget = (long) entity.get(Constants.INITIAL_BUDGET);
                long initialInfrastructureCost =
                        (long) entity.get(Constants.INITIAL_INFRASTRUCTURE_COST);
                long energyNeeded = (long) entity.get(Constants.ENERGY_NEEDED_KW);
                String strategyTitle = (String) entity.get(Constants.PRODUCER_STRATEGY);

                SelectProducersStrategyFactory strategyFactory =
                        SelectProducersStrategyFactory.getInstance();

                EnergyChoiceStrategyType strategyType =
                        EnergyChoiceStrategyType.valueOf(strategyTitle);
                SelectProducersStrategy strategy = strategyFactory.createStrategy(strategyType);

                // Create a new distributor (entity)
                newEntity = new Distributor(id, initialBudget, contractLength,
                        initialInfrastructureCost, energyNeeded, strategyTitle, strategy);
            }

            case Constants.PRODUCER -> {
                String energyTypeString = (String) entity.get(Constants.ENERGY_TYPE);
                long maxDistributors = (long) entity.get(Constants.MAX_DISTRIBUTORS);
                double priceKW = (double) entity.get(Constants.PRICE_KW);
                long energyPerDistributor = (long) entity.get(Constants.ENERGY_PER_DISTRIBUTOR);
                EnergyType energyType = EnergyType.valueOf(energyTypeString);

                // Create a new producer (entity)
                newEntity = new Producer(id, energyType, maxDistributors, priceKW,
                        energyPerDistributor);
            }
            default -> System.out.println(Constants.INVALID_COMMAND);
        }
        return newEntity;
    }
}
