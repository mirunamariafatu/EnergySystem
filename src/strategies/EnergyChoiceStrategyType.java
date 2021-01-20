package strategies;

/**
 * Strategy types for distributors to choose their producers.
 */
public enum EnergyChoiceStrategyType {
    GREEN("GREEN"),
    PRICE("PRICE"),
    QUANTITY("QUANTITY");

    private final String label;

    public String getLabel() {
        return label;
    }

    EnergyChoiceStrategyType(String label) {
        this.label = label;
    }
}
