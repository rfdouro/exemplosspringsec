package br.org.rfdouro.apisec.security;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends CrudRepository<User, String> {
 Optional<User> findByLogin(String login);
}
