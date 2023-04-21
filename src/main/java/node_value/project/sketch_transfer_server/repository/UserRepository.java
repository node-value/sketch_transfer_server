package node_value.project.sketch_transfer_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import node_value.project.sketch_transfer_server.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByName(String name);
}