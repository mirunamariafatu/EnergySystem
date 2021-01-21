package entities;

/**
 * Class that contains general information about an entity.
 */
public abstract class Entity {
    /**
     * Entity's id.
     */
    private final long id;

    public Entity(final long id) {
        this.id = id;
    }

    public final long getId() {
        return id;
    }
}
