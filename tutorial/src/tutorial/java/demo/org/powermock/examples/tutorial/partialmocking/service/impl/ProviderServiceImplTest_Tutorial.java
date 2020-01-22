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
package demo.org.powermock.examples.tutorial.partialmocking.service.impl;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.verifyPrivate;
import static org.powermock.reflect.Whitebox.setInternalState;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import demo.org.powermock.examples.tutorial.partialmocking.dao.ProviderDao;
import demo.org.powermock.examples.tutorial.partialmocking.domain.ServiceProducer;

/**
 * The purpose of this test is to get 100% coverage of the
 * {@link ProviderServiceImpl} class without any code changes to that class. To
 * achieve this you need learn how to create partial mocks, modify internal
 * state, invoke and expect private methods.
 * <p>
 * While doing this tutorial please refer to the documentation on how to expect
 * private methods and bypass encapsulation at the PowerMock web site.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(ProviderServiceImpl.class)
public class ProviderServiceImplTest_Tutorial {
	
	private ProviderServiceImpl tested;
	private ProviderDao providerDaoMock;


	@Before
	public void setUp() {
		this.providerDaoMock = mock(ProviderDao.class);
		this.tested = new ProviderServiceImpl();
		setInternalState(this.tested, this.providerDaoMock);
	}

	@After
	public void tearDown() {
		this.providerDaoMock = null;
		this.tested = null;
	}

	@Test
	public void testGetAllServiceProviders() throws Exception {
		// given
		this.tested = spy(new ProviderServiceImpl());
		Set<ServiceProducer> expectedProducers = new HashSet<>();
		expectedProducers.add(new ServiceProducer(42, "some name"));
		doReturn(expectedProducers).when(this.tested, "getAllServiceProducers");

		// when
		Set<ServiceProducer> actualProducers = this.tested.getAllServiceProviders();

		// then
		verifyPrivate(this.tested).invoke("getAllServiceProducers");
		assertThat(actualProducers, sameInstance(expectedProducers));
	}

	@Test
	public void testGetAllServiceProviders_noServiceProvidersFound() throws Exception {
		// given
		this.tested = spy(new ProviderServiceImpl());
		doReturn(Collections.emptySet()).when(this.tested, "getAllServiceProducers");

		// when
		Set<ServiceProducer> actualProducers = this.tested.getAllServiceProviders();

		// then
		verifyPrivate(this.tested).invoke("getAllServiceProducers");
		assertThat(actualProducers, sameInstance(Collections.emptySet()));
	}

	@Test
	public void testServiceProvider_found() throws Exception {
        // given
        this.tested = spy(new ProviderServiceImpl());
        Set<ServiceProducer> producers = new HashSet<>();
        ServiceProducer expectedProducer = new ServiceProducer(42, "some name");
        producers.add(expectedProducer);
        doReturn(producers).when(this.tested, "getAllServiceProducers");

        // when
        ServiceProducer actualProducer = this.tested.getServiceProvider(42);

        // then
        verifyPrivate(this.tested).invoke("getAllServiceProducers");
        assertThat(actualProducer, sameInstance(expectedProducer));
	}

	@Test
	public void testServiceProvider_notFound() throws Exception {
		// TODO Create a partial mock of the ProviderServiceImpl mocking only the getAllServiceProducers method
		// TODO Expect the private method call to getAllServiceProducers and return null
		// TODO Replay all mock objects used
		// TODO Perform the actual test and assert that the result matches the expectations 
		// TODO Verify all mock objects used 
	}

	@Test
	public void getAllServiceProducers() throws Exception {
		// TODO Create a new ServiceArtifact and a new HashSet place the created ServiceArtifact in this set
		// TODO Expect the call to the providerDao.getAllServiceProducers(..) and return the HashSet
		// TODO Replay all mock objects used
		// TODO Perform the actual test by invoking the private "getAllServiceProducers" method. Assert that the result matches the expectations.
		// TODO Verify all mock objects used
	}

	@Test
	public void getAllServiceProducers_empty() throws Exception {
		// TODO Create a new HashSet of ServiceArtifacts
		// TODO Replay all mock objects used
		// TODO Perform the actual test by invoking the private "getAllServiceProducers" method. Assert that the result matches the expectations.
		// TODO Verify all mock objects used
	}
}
