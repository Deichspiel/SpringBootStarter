package de.tom.service;

import de.tom.db.AccountRepository;
import de.tom.entity.Account;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith( SpringRunner.class )
@SpringBootTest
public class AccountRepositoryTest {

	@Autowired
	private AccountRepository accountRepository;

	@Ignore
	@Test
	public void testDel() {
		Account account = accountRepository.findByName( "Franz" );
		if( null != account ) {
			accountRepository.delete( account );
		}
		account = accountRepository.findByName( "Fritz" );
		if( null != account ) {
			accountRepository.delete( account );
		}
		Assert.assertNull( accountRepository.findByName( "Franz" ) );
	}
}