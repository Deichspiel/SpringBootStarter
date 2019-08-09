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
	private static final String USER = "user";
	private static final String PASSWORD = "password";

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
		return initModelAndView( createTermin() );
	}

	@PostMapping("/delete")
	public ModelAndView deleteTermin( Long id ) {
		LOGGER.debug( "Post /delete with {}", id );

		final boolean isDeleted = delete( id );
		final ModelAndView modelAndView = initModelAndView( null );
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
			mv = initModelAndView( null );
			mv.getModel().put("info", messageSource.getMessage( "termin.booked", null, Locale.getDefault() ));
		} else {
			mv = initModelAndView( termin );
			mv.getModel().put("error", error);
		}
		return mv;
	}

	private boolean delete(Long id) {
		RestTemplateBuilder builder = new RestTemplateBuilder();
		RestTemplate restTemplate = builder.basicAuthentication( USER, PASSWORD ).build();
		restTemplate.delete( terminServiceUrl + "/" + id );

		return true;
	}

	private Boolean alreadyBooked( final TerminDto termin ) {
		RestTemplateBuilder builder = new RestTemplateBuilder();
		RestTemplate restTemplate = builder.basicAuthentication( USER, PASSWORD ).build();
		return restTemplate.postForObject( terminServiceUrl + "/alreadyBooked", termin, Boolean.class );
	}

	private void saveTermin( final TerminDto termin ) {
		RestTemplateBuilder builder = new RestTemplateBuilder();
		RestTemplate restTemplate = builder.basicAuthentication( USER, PASSWORD ).build();
		restTemplate.postForObject( terminServiceUrl + "/add", termin, Boolean.class );
	}

	private ModelAndView initModelAndView(final TerminDto termin) {
		ModelAndView mv = new ModelAndView();
		if( null != termin ) {
			mv.getModel().put( "termin", termin );
		} else {
			mv.getModel().put( "termin", createTermin() );
		}

		mv.setViewName( "termine" );

		RestTemplateBuilder builder = new RestTemplateBuilder();
		RestTemplate restTemplate = builder.basicAuthentication( USER, PASSWORD ).build();
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
