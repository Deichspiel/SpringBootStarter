package de.tom.service;

import de.tom.dto.AccountDto;
import de.tom.dto.RegisterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.Locale;
import java.util.Objects;

@Controller
@RequestMapping(path="/register")
public class RegisterService implements MessageSourceAware {

	@Value( "${account.restservice.url}" )
	private String accountServiceUrl;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private MessageSource messageSource;

	@Override
	public void setMessageSource( MessageSource messageSource ) {
		this.messageSource = messageSource;
	}

	@PostMapping("")
	public ModelAndView register( @ModelAttribute RegisterDto register, Model model ) {
		System.out.println("add: " + register);
		String error = null;
		if( !Objects.equals(register.getPassword(), register.getPassword1()) ) {
			error = messageSource.getMessage( "register.passwordDistinct", null, Locale.getDefault() );
		} else {
			RestTemplateBuilder builder = new RestTemplateBuilder();
			RestTemplate restTemplate = builder.basicAuthorization("user", "password").build();
			Boolean exists = restTemplate.getForObject( accountServiceUrl + "/exists/" + register.getName(), Boolean.class);
			if( null != exists && exists ) {
				error = messageSource.getMessage( "register.userExists", null, Locale.getDefault() );
			} else {
				register.setPassword( passwordEncoder.encode( register.getPassword() ) );
				AccountDto account = new AccountDto( register );
				restTemplate = builder.basicAuthorization("user", "password").build();
				final ResponseEntity<AccountDto> entity = restTemplate.postForEntity( accountServiceUrl + "/register", account, AccountDto.class );
				final AccountDto accountDto = entity.getBody();
				System.out.println("registered: " + accountDto);
			}
		}
		ModelAndView mv = new ModelAndView();
		mv.setViewName( "login" );
		if( null != error ) {
			mv.getModel().put( "error", error );
			mv.getModel().put( "register", register );
			mv.getModel().put( "tab", "Registertab" );
		} else {
			mv.getModel().put( "info", messageSource.getMessage("register.success", new Object[]{register.getName()}, Locale.getDefault()) );
			mv.getModel().put( "register", new RegisterDto() );
			mv.getModel().put( "tab", "Logintab" );
		}
		return mv;
	}
}
