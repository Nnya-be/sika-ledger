package xellenet.sikaledger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xellenet.sikaledger.domain.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

}
