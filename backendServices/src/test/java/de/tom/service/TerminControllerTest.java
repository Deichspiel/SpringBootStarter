package de.tom.service;

import de.tom.db.TerminRepository;
import de.tom.entity.Termin;
import de.tom.entity.TerminList;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@RunWith( SpringRunner.class )
@SpringBootTest
public class TerminControllerTest {

	@MockBean
//	@Autowired
	private TerminRepository terminRepository;

	@Autowired
	private TerminController terminController;

	@Test
	public void testGetTermin() {
		LocalDateTime now = LocalDateTime.now();
		Termin termin = new Termin( "Bla", "Franz", now, now.plusHours(1L) );
		Mockito.when( terminRepository.findAll( Mockito.any(Sort.class) ) ).thenReturn( Collections.singletonList(termin) );
		final ResponseEntity<TerminList> responseEntity = terminController.getTermine();
		Assert.assertNotNull( responseEntity );
		final TerminList termine = responseEntity.getBody();
		Assert.assertNotNull( termine );
		Assert.assertNotNull( termine.getTermine() );
		final Iterator<Termin> terminIterator = termine.getTermine().iterator();
		Assert.assertTrue( terminIterator.hasNext() );
		Termin termin1 = terminIterator.next();
		Assert.assertEquals( termin, termin1 );
		Assert.assertFalse( terminIterator.hasNext() );
	}

	@Ignore
	@Test
	public void deleteTermin() {
		final List<Termin> termine = terminRepository.findByUser( "Franz" );
		termine.forEach( terminRepository::delete );
		LocalDateTime now = LocalDateTime.now();
		final Termin t1 = new Termin( "Bla", "Franz", now, now.plusHours( 1L ) );
		final Termin t2 = terminRepository.save( t1 );
		Assert.assertEquals( t1, t2 );
	}
}