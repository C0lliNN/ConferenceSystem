package com.raphael.conferenceapp.auth.persistence;

import com.raphael.conferenceapp.auth.domain.User;
import com.raphael.conferenceapp.auth.usecase.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JPAUserRepository extends UserRepository, JpaRepository<User, Long> {
}
