package com.basic.expensecalculatorbackend.repositories;

import com.basic.expensecalculatorbackend.data.model.entities.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfileEntity, Long> {
}
