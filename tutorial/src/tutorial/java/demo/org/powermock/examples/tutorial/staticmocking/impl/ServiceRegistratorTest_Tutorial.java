/*
 * Copyright 2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package demo.org.powermock.examples.tutorial.staticmocking.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.reflect.Whitebox.getInternalState;
import static org.powermock.reflect.Whitebox.setInternalState;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import demo.org.powermock.examples.tutorial.staticmocking.osgi.BundleContext;
import demo.org.powermock.examples.tutorial.staticmocking.osgi.ServiceRegistration;

/**
 * The purpose of this test is to get 100% coverage of the
 * {@link ServiceRegistrator} class without any code changes to that class. To
 * achieve this you need learn how to mock static methods as well as how to set
 * and get internal state of an object.
 * <p>
 * While doing this tutorial please refer to the documentation on how to mock
 * static methods and bypass encapsulation at the PowerMock web site.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(IdGenerator.class)
public class ServiceRegistratorTest_Tutorial {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private BundleContext bundleContextMock;
	private ServiceRegistration serviceRegistrationMock;
	private ServiceRegistrator tested;

	@Before
	public void setUp() {
		this.bundleContextMock = mock(BundleContext.class);
		this.serviceRegistrationMock = mock(ServiceRegistration.class);
		this.tested = new ServiceRegistrator();
		setInternalState(this.tested, this.bundleContextMock);
		mockStatic(IdGenerator.class);
	}

	@After
	public void tearDown() {
		this.bundleContextMock = null;
		this.serviceRegistrationMock = null;
		this.tested = null;
	}

	/**
	 * Test for the {@link ServiceRegistrator#registerService(String, Object)}
	 * method.
	 * 
	 * @throws Exception
	 *             If an error occurs.
	 */
	@Test
	public void testRegisterService() throws Exception {
		// given
		String name = "Focker";
		long expectedId = 42L;
		when(this.bundleContextMock.registerService(eq(name), any(), any())).thenReturn(this.serviceRegistrationMock);
		when(IdGenerator.generateNewId()).thenReturn(expectedId);

		// when
		long actualId = this.tested.registerService(name, new Object());

		// then
		Map<Long, ServiceRegistration> serviceRegistrations = getInternalState(this.tested, "serviceRegistrations");
		assertThat(actualId, equalTo(expectedId));
		assertThat(serviceRegistrations.get(expectedId), is(this.serviceRegistrationMock));
		assertThat(serviceRegistrations.size(), equalTo(1));
	}

	/**
	 * Test for the {@link ServiceRegistrator#unregisterService(long)} method.
	 * 
	 * @throws Exception
	 *             If an error occurs.
	 */
	@Test
	public void testUnregisterService() throws Exception {
		// given
		Map<Long, ServiceRegistration> serviceRegistrations = new HashMap<>();
		serviceRegistrations.put(42L, this.serviceRegistrationMock);
		setInternalState(this.tested, serviceRegistrations);

		// when
		this.tested.unregisterService(42L);

		// then
		verify(this.serviceRegistrationMock).unregister();
		assertTrue(serviceRegistrations.isEmpty());
	}

	/**
	 * Test for the {@link ServiceRegistrator#unregisterService(long)} method
	 * when the ID doesn't exist.
	 * 
	 * @throws Exception
	 *             If an error occurs.
	 */
	@Test
	public void testUnregisterService_idDoesntExist() throws Exception {
		long id = 0L;
		this.thrown.expect(IllegalStateException.class);
		this.thrown.expectMessage("Registration with id " + id + " has already been removed or has never been registered");
		setInternalState(this.tested, new HashMap<>());

		this.tested.unregisterService(id);
	}
}
