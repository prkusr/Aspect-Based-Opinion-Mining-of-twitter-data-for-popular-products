package edu.bigData.sparkBusters.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

public class CustomConfiguration {
	private final Environment env;

	public CustomConfiguration(Environment env) {
		this.env = env;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public FilterRegistrationBean corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.setAllowedOrigins(Arrays.asList(env.getProperty("allowOrigin").split(",")));
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);

		FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
		bean.setOrder(0);

		return bean;
	}
}
