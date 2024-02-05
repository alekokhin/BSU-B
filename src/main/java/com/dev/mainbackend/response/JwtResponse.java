package com.dev.mainbackend.response;

import lombok.Data;

@Data
public class JwtResponse {
	private String accessToken;
	private String refreshToken;
	private String type = "Bearer";
	private String _id;
	private String username;


	public JwtResponse(String accessToken, String refreshToken, String _id, String username) {
		this.accessToken = accessToken;
		this.refreshToken=refreshToken;
		this._id = _id;
		this.username = username;

	}

}
