package de.tom.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

@Configuration
public class ThymeleafConfiguration {

//	private static final String CHARACTER_ENCODING = "UTF-8";
//	private static final String VIEWS = "";

//	@Bean
//	public ITemplateResolver templateResolver() {
//		SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
//		resolver.setPrefix(VIEWS);
//		resolver.setSuffix(".html");
//		resolver.setTemplateMode( TemplateMode.HTML);
//		resolver.setCharacterEncoding(CHARACTER_ENCODING);
//		resolver.setCacheable(false);
//		return resolver;
//	}

	@Bean
	public Java8TimeDialect java8TimeDialect() {
		return new Java8TimeDialect();
	}
}
