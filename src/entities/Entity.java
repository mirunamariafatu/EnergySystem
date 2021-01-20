package entities;

/**
 * Class that contains general information about an entity.
 */
public abstract class Entity {
    /**
     * Entity's id.
     */
    private final long id;

    /**
     * Boolean flag which indicates whether
     * the entity is bankrupt or not.
     */
    private boolean isBankrupt = false;

    public Entity(final long id) {
        this.id = id;
    }

    public final long getId() {
        return id;
    }

    public final boolean getIsBankrupt() {
        return isBankrupt;
    }

    public final void setIsBankrupt(final boolean bankrupt) {
        isBankrupt = bankrupt;
    }
}
