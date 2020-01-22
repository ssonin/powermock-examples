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
package demo.org.powermock.examples.tutorial.domainmocking.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.withSettings;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import demo.org.powermock.examples.tutorial.domainmocking.EventService;
import demo.org.powermock.examples.tutorial.domainmocking.PersonService;
import demo.org.powermock.examples.tutorial.domainmocking.domain.BusinessMessages;
import demo.org.powermock.examples.tutorial.domainmocking.domain.Person;
import demo.org.powermock.examples.tutorial.domainmocking.domain.SampleServiceException;

/**
 * The purpose of this test is to get 100% coverage of the
 * {@link SampleServiceImpl} class without any code changes to that class. To
 * achieve this you need learn how to mock instantiation of domain objects.
 * <p>
 * While doing this tutorial please refer to the documentation on how to mock
 * construction of new objects at the PowerMock web site.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ SampleServiceImpl.class, BusinessMessages.class, Person.class })
public class SampleServiceImplTest_Tutorial {

	private SampleServiceImpl tested;
	private PersonService personServiceMock;
	private EventService eventServiceMock;

	@Before
	public void setUp() {
		this.personServiceMock = mock(PersonService.class);
		this.eventServiceMock = mock(EventService.class);
		this.tested = new SampleServiceImpl(this.personServiceMock, this.eventServiceMock);
	}

	@After
	public void tearDown() {
		this.personServiceMock = null;
		this.eventServiceMock = null;
		this.tested = null;
	}

	@Test
	public void testCreatePerson() throws Exception {
		// given
		BusinessMessages messages = mock(BusinessMessages.class);
		Person person = mock(Person.class, withSettings().useConstructor("first name", "last name"));
		whenNew(BusinessMessages.class).withNoArguments().thenReturn(messages);
		whenNew(Person.class).withArguments("first name", "last name").thenReturn(person);
		when(messages.hasErrors()).thenReturn(false);

		// when
		boolean expected = this.tested.createPerson("first name", "last name");

		// then
		assertTrue(expected);
	}

	@Test
	public void testCreatePerson_error() throws Exception {
		// given
		BusinessMessages messages = mock(BusinessMessages.class);
		Person person = mock(Person.class, withSettings().useConstructor("first name", "last name"));
		whenNew(BusinessMessages.class).withNoArguments().thenReturn(messages);
		whenNew(Person.class).withArguments("first name", "last name").thenReturn(person);
		when(messages.hasErrors()).thenReturn(true);

		// when
		boolean expected = this.tested.createPerson("first name", "last name");

		// given
		assertFalse(expected);
		verify(this.eventServiceMock).sendErrorEvent(person, messages);
	}

	@Test(expected = SampleServiceException.class)
	public void testCreatePerson_illegalName() throws Exception {
		whenNew(Person.class).withAnyArguments().thenThrow(new IllegalArgumentException());
		this.tested.createPerson("first name", "last name");
	}
}
