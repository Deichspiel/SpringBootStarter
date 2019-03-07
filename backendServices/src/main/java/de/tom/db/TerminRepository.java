package de.tom.db;

import de.tom.entity.Termin;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TerminRepository extends PagingAndSortingRepository<Termin, Long> {
	List<Termin> findByUser( String user );
	List<Termin> findByResource( String resource );

	@Query("select count(t) > 0 from Termin t where t.resource = :resource and ((t.start <= :start and t.end >= :start) or (t.start <= :end and t.end >= :end))")
	boolean alreadyBooked( @Param( "resource" ) String resource, @Param( "start" ) LocalDateTime start, @Param( "end" ) LocalDateTime end );
}
