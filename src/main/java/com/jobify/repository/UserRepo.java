package com.jobify.repository;

import com.jobify.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User>
{
    Optional<User> findByEmailID(String email);
}
