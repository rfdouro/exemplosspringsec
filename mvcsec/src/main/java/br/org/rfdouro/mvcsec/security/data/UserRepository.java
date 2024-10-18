package br.org.rfdouro.mvcsec.security.data;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

// repositório para manipular registros de User
@Repository
public interface UserRepository extends CrudRepository<User, String> {
 Optional<User> findByLogin(String login);
}
