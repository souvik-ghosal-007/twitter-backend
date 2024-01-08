package com.souvik.twitter.repositories;

import com.souvik.twitter.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByEmail(String email);

    @Query(value = "SELECT DISTINCT u From User u Where u.fullName Like %:query% OR u.email Like %:query%")
    List<User> searchUsers(@Param("query") String query);
}
