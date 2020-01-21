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
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.reflect.Whitebox.getInternalState;
import static org.powermock.reflect.Whitebox.setInternalState;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
// TODO Specify the PowerMock runner
// TODO Specify which classes that must be prepared for test
@RunWith(PowerMockRunner.class)
@PrepareForTest(IdGenerator.class)
public class ServiceRegistratorTest_Tutorial {

	private BundleContext bundleContextMock;
	private ServiceRegistration serviceRegistrationMock;
	private ServiceRegistrator tested;

	@Before
	public void setUp() {
		// TODO Create a mock object of the BundleContext and ServiceRegistration classes
		this.bundleContextMock = mock(BundleContext.class);
		this.serviceRegistrationMock = mock(ServiceRegistration.class);
		this.tested = new ServiceRegistrator();
		setInternalState(this.tested, this.bundleContextMock);

		// TODO Prepare the IdGenerator for static mocking
		mockStatic(IdGenerator.class);

		// TODO Create a new instance of SampleServiceImpl and pass in the created mock objects to the constructor

	}

	@After
	public void tearDown() {		
		// TODO Set all references to null
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
		Object serviceImpl = new Object();
		long expectedId = 42L;
		when(this.bundleContextMock.registerService(name, serviceImpl, null)).thenReturn(this.serviceRegistrationMock);
		when(IdGenerator.generateNewId()).thenReturn(expectedId);

		// when
		long actualId = this.tested.registerService(name, serviceImpl);

		// then
		Map<Long, ServiceRegistration> serviceRegistrations = getInternalState(this.tested, Map.class);
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
		// TODO Create a new HashMap of ServiceRegistration's and add a new ServiceRegistration to the map.
		// TODO Set the new HashMap to the serviceRegistrations field in the tested instance 
		// TODO Expect the call to serviceRegistrationMock.unregister()
		// TODO Replay all mock objects used
		// TODO Perform the actual test and assert that the result matches the expectations
		// TODO Verify all mock objects used
		// TODO Assert that the serviceRegistrations map in the test instance has been updated correctly
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
		// TODO Create a new HashMap of ServiceRegistration's and set it to the serviceRegistrations field in the tested instance 
		// TODO Expect the call to serviceRegistrationMock.unregister() and throw an IllegalStateException
		// TODO Replay all mock objects used
		// TODO Perform the actual test and assert that the result matches the expectations
		// TODO Verify all mock objects used
		// TODO Assert that the serviceRegistrations map in the test instance has not been updated
	}
}
