package es.softtek.jwtDemo.controller;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import es.softtek.jwtDemo.dto.User;
import es.softtek.jwtDemo.service.DBService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
public class UserController {

	private final DBService DBService;

	public UserController(DBService DBService) {this.DBService = DBService;}

	// Controlador para o login
	@PostMapping("login")
	public User login(@RequestParam("user") String username, @RequestParam("adm") String adm, @RequestParam("password") String pwd) throws Exception {

		String role = "ROLE_USER";
		if(Objects.equals(adm, "y")) {role = "ROLE_ADMINISTRADOR";}
		String token = getJWTToken(username,role);
		if(DBService.existeUtilizador(username,pwd)){
			return new User(username,token,role);
		}
		return null;
	}

	private String getJWTToken(String username, String role) {
	
		String secretKey = "mySecretKey";  //   DEVIA SER LIDA via configuracoes!!!
		
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils

				.commaSeparatedStringToAuthorityList(role);
		
		String token = Jwts
				.builder()
				.setId("softtekJWT")
				.setSubject(username)
				.claim("authorities",
						grantedAuthorities.stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 600000))
				.signWith(SignatureAlgorithm.HS512,
						secretKey.getBytes()).compact();

		return "Bearer " + token;
	}
}
