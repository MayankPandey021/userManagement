
package com.example.userManagement.repository;

import com.example.userManagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

//    static Optional<User> findById(Long id);

    Optional<User> findByUsername(String username); // This is a method name-based query (or Derived Query)

    @Query("SELECT u FROM User u WHERE u.isDeleted = false")
    List<User> findAllNonDeletedUsers();  // When findAllNonDeletedUsers() is called, run this JPQL query.
}