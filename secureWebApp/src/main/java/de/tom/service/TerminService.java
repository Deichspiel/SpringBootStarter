package de.tom.service;

import de.tom.dto.TerminDto;
import de.tom.dto.TerminDtoList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.Locale;

@Controller
@RequestMapping(path="/termine")
public class TerminService implements MessageSourceAware {

	private static final Logger LOGGER = LoggerFactory.getLogger( TerminService.class );

	@Value( "${termin.restservice.url}" )
	private String terminServiceUrl;

	private MessageSource messageSource;

	@Override
	public void setMessageSource( MessageSource messageSource ) {
		this.messageSource = messageSource;
	}

	@GetMapping({"", "/list"})
	public ModelAndView getTermine(Model model) {
		LOGGER.debug( "Get /list" );
		return initModelAndView( createTermin(), false );
	}

	@PostMapping("/delete")
	public ModelAndView deleteTermin( Long id ) {
		LOGGER.debug( "Post /delete with {}", id );

		final boolean isDeleted = delete( id );
		final ModelAndView modelAndView = initModelAndView( null, true );
		modelAndView.getModel().put( "isDeleted", isDeleted );
		return modelAndView;
	}

	@PostMapping("/add")
	public ModelAndView addTermin( @ModelAttribute TerminDto termin, Model model) {
		LOGGER.debug( "Post /add with {}", termin );

		String error = null;
		if( termin.getStart().isAfter( termin.getEnd() )) {
			error = messageSource.getMessage( "termin.startAfterEnd", null, Locale.getDefault() );
		} else if( alreadyBooked(termin) ) {
			error = messageSource.getMessage("termin.resourceAlreadyBooked", new Object[]{termin.getResource()}, Locale.getDefault());
		} else {
			saveTermin( termin );
		}
		ModelAndView mv;
		if( null == error ) {
			mv = initModelAndView( null, true );
			mv.getModel().put("info", messageSource.getMessage( "termin.booked", null, Locale.getDefault() ));
		} else {
			mv = initModelAndView( termin, true );
			mv.getModel().put("error", error);
		}
		return mv;
	}

	private boolean delete(Long id) {
		RestTemplateBuilder builder = new RestTemplateBuilder();
		RestTemplate restTemplate = builder.basicAuthorization("user", "password").build();
		restTemplate.delete( terminServiceUrl + "/" + id );

		return true;
	}

	private boolean alreadyBooked( final TerminDto termin ) {
		RestTemplateBuilder builder = new RestTemplateBuilder();
		RestTemplate restTemplate = builder.basicAuthorization("user", "password").build();
		final Boolean b = restTemplate.postForObject( terminServiceUrl + "/alreadyBooked", termin, Boolean.class );
		return b;
	}

	private void saveTermin( final TerminDto termin ) {
		RestTemplateBuilder builder = new RestTemplateBuilder();
		RestTemplate restTemplate = builder.basicAuthorization("user", "password").build();
		restTemplate.postForObject( terminServiceUrl + "/add", termin, Boolean.class );
	}

	private ModelAndView initModelAndView(final TerminDto termin, final boolean redirect) {
		ModelAndView mv = new ModelAndView();
		if( null != termin ) {
			mv.getModel().put( "termin", termin );
		} else {
			mv.getModel().put( "termin", createTermin() );
		}

		mv.setViewName( "termine" );

		RestTemplateBuilder builder = new RestTemplateBuilder();
		RestTemplate restTemplate = builder.basicAuthorization( "user", "password" ).build();
		final TerminDtoList termine = restTemplate.getForObject( terminServiceUrl, TerminDtoList.class );

		if( null != termine ) {
			mv.getModel().put( "termine", termine.getTermine() );
		}
		return mv;
	}

	private TerminDto createTermin() {
		TerminDto termin = new TerminDto();
		termin.setUser( SecurityContextHolder.getContext().getAuthentication().getName() );

		return termin;
	}
}
