package com.app.meetly.persistence.repository;


import com.app.meetly.models.entity.Operator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OperatorRepository extends JpaRepository<Operator, Long> {

    Optional<Operator> findOperatorByName(String name);

    @Query(value = "select op.id from mtly_operator op where op.id NOT IN :excludedIDs LIMIT 1", nativeQuery = true)
    Optional<Long> findAvailableOperatorByIDs(List<Long> excludedIDs);

    Optional<Operator> findFirstByOrderById();
}
