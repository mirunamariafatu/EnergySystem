package strategies;

/**
 * Class that implements Factory design pattern, in order to
 * create multiple strategies.
 */
public final class SelectProducersStrategyFactory {

    private static SelectProducersStrategyFactory instance = null;

    private SelectProducersStrategyFactory() {
    }

    /**
     * Gets the instance.
     *
     * @return the instance
     */
    public static SelectProducersStrategyFactory getInstance() {
        if (instance == null) {
            instance = new SelectProducersStrategyFactory();
        }

        return instance;
    }

    /**
     * Method that creates a custom strategy.
     *
     * @param strategyType the strategy type
     * @return the strategy created
     */
    public SelectProducersStrategy createStrategy(EnergyChoiceStrategyType strategyType) {

        switch (strategyType) {
            case GREEN -> {

                // Create a new green strategy
                return new GreenStrategy();
            }
            case PRICE -> {

                // Create a new price strategy
                return new PriceStrategy();
            }
            case QUANTITY -> {

                // Create a new quantity strategy
                return new QuantityStrategy();
            }
            default -> System.out.println("Invalid Command!");
        }

        return null;
    }
}
