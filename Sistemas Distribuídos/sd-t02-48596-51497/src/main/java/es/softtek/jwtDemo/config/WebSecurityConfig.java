package es.softtek.jwtDemo.config;

import es.softtek.jwtDemo.security.JWTAuthorizationFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers("/").authenticated()
                .antMatchers(HttpMethod.POST,"/atualizarTipoUtilizador").hasRole("ADMINISTRADOR")
                .antMatchers(HttpMethod.GET,"/procurarArtistasPorEstado").hasRole("ADMINISTRADOR")
                .antMatchers(HttpMethod.POST,"/aprovarArtista/*").hasRole("ADMINISTRADOR")
                .antMatchers(HttpMethod.GET,"/consultarDescricaoEImagem/*").hasRole("ADMINISTRADOR")
                .antMatchers(HttpMethod.POST,"/alterarDescFoto/*").hasRole("ADMINISTRADOR")

                .anyRequest().authenticated();
    }
}