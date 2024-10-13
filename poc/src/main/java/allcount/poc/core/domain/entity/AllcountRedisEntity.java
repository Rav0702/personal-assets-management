package allcount.poc.core.domain.entity;

import java.io.Serializable;

/**
 * Base class for all entities that are stored in Redis.
 */
public abstract class AllcountRedisEntity implements Serializable {
    protected static final String REDIS_KEY_SEPARATOR = "|";

    protected String id;

    /**
     * Gets the id of the entity formatted for Redis.
     *
     * @return the id of the entity.
     */
    protected abstract String generateRedisId();
}
