/*
package de.oostech.tanglebayranking.test;

import de.oostech.tanglebayranking.dao.CommandDAO;
import de.oostech.tanglebayranking.dao.NodeDAO;
import de.oostech.tanglebayranking.service.CommandService;
import de.oostech.tanglebayranking.service.NodeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
public class ScoreTest {
	@Autowired
	private TestEntityManager entityManager;

	@TestConfiguration
	static class NodeServiceImplTestContextConfiguration {

		@Bean
		public NodeService nodeServiceService() {
			return new NodeService();
		}
	}

	@Autowired
	private NodeService nodeService;

	@MockBean
	private NodeDAO nodeDAO;

	@TestConfiguration
	static class CommandServiceImplTestContextConfiguration {

		@Bean
		public CommandService commandServiceService() {
			return new CommandService();
		}
	}

	@Autowired
	private CommandService commandService;

	@MockBean
	private CommandDAO commandDAO;

	@Test
	public void whenValidName_thenEmployeeShouldBeFound() {
		String name = "alex";

*/
/*		assertThat(found.getName())
				.isEqualTo(name);*//*

	}
}
*/
