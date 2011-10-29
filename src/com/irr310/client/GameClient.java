package com.irr310.client;

import com.irr310.client.game.Player;
import com.irr310.client.network.LoginRequest;
import com.irr310.client.network.NetworkServer;


public class GameClient {

	public static GameClient instance = null;

	public Player player;

	private NetworkServer network;
	
	public static GameClient getInstance() {
		return instance;
	}

	public GameClient() {
		instance = this;
		
		// Start non logged
		player = null;
		network = new NetworkServer("127.0.0.10", 22310);
	}

	public void login(String login, String password) {
		if(isLogged()) {
			logout();
		}
		
		LoginRequest loginRequest = new LoginRequest(login, password);
		loginRequest.sendAndWait(network);
		
	}
	
	private void logout() {
	}

	public NetworkServer getNetwork() {
		return network;
	}
	
	public boolean isLogged() {
		return player != null;
	}
	
}
