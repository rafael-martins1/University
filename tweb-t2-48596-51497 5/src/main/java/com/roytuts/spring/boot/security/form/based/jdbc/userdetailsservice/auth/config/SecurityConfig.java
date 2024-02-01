package com.roytuts.spring.boot.security.form.based.jdbc.userdetailsservice.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.roytuts.spring.boot.security.form.based.jdbc.userdetailsservice.auth.service.UserAuthService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserAuthService userAuthService;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userAuthService).passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
                    .authorizeRequests()// , authorize request
                    .antMatchers("/acessoReservado*").authenticated()
					.antMatchers("/registerEvent*").hasRole("STAFF")
					.antMatchers("/registerTimestamp*").hasRole("STAFF")
					.antMatchers("/registerParticipant*").hasRole("ATLETA")
					.antMatchers("/checkRegistrations*").hasRole("ATLETA")
                    .antMatchers("/*").permitAll()
                    .and()
                    .formLogin().loginPage("/login")// specifies the location of the log in page
                    .loginProcessingUrl("/j_spring_security_check")// login submit target
                    .defaultSuccessUrl("/")// default-target-url,
                    .failureUrl("/login?error")// authentication-failure-url,
                    .usernameParameter("username")// overrides Spring's default
                    .passwordParameter("password");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
