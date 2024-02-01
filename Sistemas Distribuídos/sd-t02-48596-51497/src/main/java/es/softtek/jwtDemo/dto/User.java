package es.softtek.jwtDemo.dto;

public class User {

	private final String user;
	private final String token;

	public User(String username, String token, String role) {
		this.user = username;
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	@Override
	public String toString() {
		return "User{" +
				"user='" + user + '\'' +
				", token='" + token + '\'' +
				'}';
	}
}
