# staticmock
make static method could be easy mock

### Example
```java
package com.worescloud.os.staticmock;

import org.mockito.InjectMocks;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.worescloud.os.staticmock.StaticMockito.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

@SuppressWarnings("ALL")
public class RegistrationServiceTest {

	static {
		enhanceClass("com.worescloud.os.staticmock.ClientUtility");
	}

	@InjectMocks
	private RegistrationService registrationService;

	@BeforeMethod
	public void beforeMethod() {
		initMocks(this);

		initMock(ClientUtility.class);
	}

	@Test
	public void shouldUnregisterClient() {
		// Given
		String clientName = "Temporary Client";
		when(ClientUtility.toClientId(clientName)).thenReturn("T01");

		// Execute
		boolean result = registrationService.unregisterClient(clientName);

		// Assert
		assertTrue(result);

		// Verify
		//noinspection AccessStaticViaInstance
		verify(mocker(ClientUtility.class)).toClientId(clientName);
	}

	@Test
	public void shouldUnregisterClientFailed() {
		// Given
		String clientName = "Temporary Client";
		when(ClientUtility.toClientId(clientName)).thenReturn("C01");

		// Execute
		boolean result = registrationService.unregisterClient(clientName);

		// Assert
		assertFalse(result);

		// Verify
		verify(mocker(ClientUtility.class)).toClientId(clientName);
		verify(mocker(ClientUtility.class), never()).toClientId("other client");
	}

	@Test
	public void shouldObtainClientName() {

		// Given
		String clientId = "C01";
		when(ClientUtility.toClientName(clientId)).thenCallRealMethod();

		// Execute
		String result = registrationService.toClientName(clientId);

		// Assert
		assertEquals(result, "clientName with ID (C01)");
	}

}
```