package com.worescloud.os.staticmock;

import java.util.Arrays;
import java.util.List;

public class RegistrationService {

	private static final List<String> INUSED_CLIENTS = Arrays.asList("C01", "C02");

	public boolean unregisterClient(String clientName) {
		String clientId = ClientUtility.toClientId(clientName);
		return !INUSED_CLIENTS.contains(clientId);
	}

}