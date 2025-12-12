package com.workspark.authenticationservice.repository;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.workspark.authenticationservice.model.Oauth2RegisteredClientEntity;
import com.workspark.authenticationservice.projections.Oauth2RegisteredClientProjection;

@Repository
public interface Oauth2RegisteredClientRepository extends CrudRepository<Oauth2RegisteredClientEntity, Serializable> {

	Optional<Oauth2RegisteredClientProjection> findByRegistrationId(String registrationId);
}
