package guru.sfg.brewery.repositories.security;

import guru.sfg.brewery.domain.security.LoginSuccsess;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginSuccessRepository extends JpaRepository<LoginSuccsess, Integer> {
}
