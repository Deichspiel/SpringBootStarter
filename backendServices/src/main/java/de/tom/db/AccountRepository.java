package de.tom.db;

import de.tom.entity.Account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends CrudRepository<Account, Long> {
	Account findByName( String name );

	@Query("select count(a) > 0 from Account a where a.name = :name")
	boolean exists( @Param( "name" ) String name );
}
