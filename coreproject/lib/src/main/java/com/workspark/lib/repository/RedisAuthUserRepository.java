package com.workspark.lib.repository;

import com.workspark.lib.models.entity.RedisAuthUser;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing RedisAuthUser entities.
 * Extends CrudRepository to inherit basic CRUD operations.
 */
@Repository
@ConditionalOnProperty(name = "workspark.redis", havingValue = "true", matchIfMissing = true)
public interface RedisAuthUserRepository extends CrudRepository<RedisAuthUser, String>{
}
