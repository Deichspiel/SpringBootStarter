package de.tom.service;

import de.tom.dto.AccountDto;
import de.tom.dto.RegisterDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.NotEmpty;
import java.util.Collections;
import java.util.Set;

@Service
@Controller
@RequestMapping(path="/login")
public class AccountService implements UserDetailsService {

	@Value( "${account.restservice.url}" )
	private String accountServiceUrl;

	@Override
	public UserDetails loadUserByUsername( @NotEmpty String name ) throws UsernameNotFoundException {
		if( null == name || name.isEmpty() ) {
			throw new UsernameNotFoundException( "Es wurde kein Benutzername angegeben!" );
		}
		RestTemplateBuilder builder = new RestTemplateBuilder();
		RestTemplate restTemplate = builder.basicAuthorization("user", "password").build();
		try {
			Boolean exists = restTemplate.getForObject( accountServiceUrl + "/exists/" + name, Boolean.class );
			if( null != exists && exists ) {
				AccountDto account = restTemplate.getForObject( accountServiceUrl + "/current/" + name, AccountDto.class );
				if( null != account ) {
					final Set<GrantedAuthority> authorities = Collections.singleton( createAuthority( account ) );
					login( account, authorities );
					return createUser( account, authorities );
				}
			}
		} catch( HttpStatusCodeException hce ) {
			if( hce.getStatusCode() != HttpStatus.NOT_FOUND ) {
				System.err.println( "(" + hce.getClass().getSimpleName() + "): " + hce.getLocalizedMessage() );
				throw hce;
			}
		}
		throw new UsernameNotFoundException( "Benutzer mit Namen '" + name + "' nicht gefunden!" );
	}

	private void login( AccountDto account, final Set<GrantedAuthority> authorities ) {
		SecurityContextHolder.getContext().setAuthentication( authenticate(account, authorities) );
	}

	private Authentication authenticate( AccountDto account, final Set<GrantedAuthority> authorities ) {
		return new UsernamePasswordAuthenticationToken( createUser(account, authorities), null, authorities );
	}

	private User createUser( AccountDto account, Set<GrantedAuthority> authorities ) {
		return new User(account.getName(), account.getPassword(), authorities );
	}

	private GrantedAuthority createAuthority( AccountDto account) {
		return new SimpleGrantedAuthority(account.getRole());
	}

	@GetMapping("")
	public ModelAndView login() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName( "login" );
		mv.getModel().put( "register", new RegisterDto() );
		mv.getModel().put( "tab", "Logintab" );
		return mv;
	}
}
