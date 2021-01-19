package entities;

/**
 * Class that contains general information about an entity
 */
public abstract class Entity {
    /**
     * Entity's id
     */
    private final long id;
    /**
     * Entity's budget
     */
    private long budget;
    /**
     * Boolean flag which indicates whether
     * the entity is bankrupt or not
     */
    private boolean isBankrupt = false;

    public Entity(final long id, final long initialBudget) {
        this.id = id;
        this.budget = initialBudget;
    }

    public final long getId() {
        return id;
    }

    public final long getBudget() {
        return budget;
    }

    public final void setBudget(final long budget) {
        this.budget = budget;
    }

    public final boolean getIsBankrupt() {
        return isBankrupt;
    }

    public final void setIsBankrupt(final boolean bankrupt) {
        isBankrupt = bankrupt;
    }
}
