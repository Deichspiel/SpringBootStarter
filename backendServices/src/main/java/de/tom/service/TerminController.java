package de.tom.service;

import de.tom.db.TerminRepository;
import de.tom.entity.Termin;
import de.tom.entity.TerminList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Controller
@RequestMapping(path="/termine")
public class TerminController {

	private static final Logger LOGGER = LoggerFactory.getLogger( TerminController.class );

	@Autowired
	private TerminRepository terminRepository;

	@GetMapping({"", "/list"})
	public ResponseEntity<TerminList> getTermine() {
		LOGGER.debug( "Get /list" );

		final Iterable<Termin> termine = terminRepository.findAll( Sort.by( "resource", "start", "user" ) );
		TerminList terminList = new TerminList( termine );
		return ResponseEntity.ok(terminList);
	}

	@PostMapping("/alreadyBooked")
	public ResponseEntity<Boolean> alreadyBooked( @RequestBody Termin termin ) {
		LOGGER.debug( "Post /alreadyBooked with {}", termin );

		final boolean exists = terminRepository.alreadyBooked( termin.getResource(), termin.getStart(), termin.getEnd() );
		return new ResponseEntity<>( exists, HttpStatus.OK );
	}

	@DeleteMapping("/{id}")
	public ResponseEntity deleteTermin(@PathVariable("id") Long id) {
		LOGGER.debug( "Delete /{}", id );

		ResponseEntity<Boolean> responseEntity;
		Optional<Termin> optionalTermin = terminRepository.findById( id );
		if( optionalTermin.isPresent() ) {
			LOGGER.debug( "Delete {}", optionalTermin.get() );
			terminRepository.deleteById( id );
			responseEntity = new ResponseEntity<>( HttpStatus.OK );
		} else {
			responseEntity = new ResponseEntity<>( HttpStatus.NOT_MODIFIED );
		}
		return responseEntity;
	}

	@PostMapping("/add")
	public ResponseEntity addTermin( @RequestBody Termin termin ) {
		LOGGER.debug( "Post /add with {}", termin );

		if( termin.getStart().isAfter( termin.getEnd() )) {
			throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "Der Beginn des Termins liegt hinter seinem Ende!" );
		} else if( terminRepository.alreadyBooked( termin.getResource(), termin.getStart(), termin.getEnd() ) ) {
			throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "Die Ressource '" + termin.getResource() + "' ist zu dem Termin bereits gebucht." );
		} else {
			terminRepository.save( termin );
			return new ResponseEntity<>(true, HttpStatus.OK );
		}
	}
}
