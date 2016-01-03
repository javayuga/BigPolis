package br.net.neuromancer.bigpolis.tserd14;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.util.Arrays;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.net.neuromancer.bigpolis.tserd14.configurations.BasicConfiguration;
import br.net.neuromancer.bigpolis.tserd14.configurations.RabbitMqConfiguration;
import br.net.neuromancer.bigpolis.tserd14.crawlers.CandidatesByRegionByOfficeCrawler;
import br.net.neuromancer.bigpolis.tserd14.executors.FullUpdateExecutor;
import br.net.neuromancer.bigpolis.tserd14.models.GeoUF;
import br.net.neuromancer.bigpolis.tserd14.models.PolCargo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { BasicConfiguration.class, RabbitMqConfiguration.class })
@ActiveProfiles("prod")
public class FullUpdateExecutorTests {
	protected final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	protected Environment env;

	@Autowired
	protected URI rabbitmqURI;

	@Autowired
	protected RabbitTemplate rabbitTemplate;

	@Autowired
	protected FullUpdateExecutor fullUpdateOrchestrator;

	@Autowired
	protected CandidatesByRegionByOfficeCrawler candidatesByRegionByOfficeCrawler;

	@Autowired 
	protected RabbitAdmin rabbitAdmin;
	
	
	@Value("${rabbitmq.commandExchange}")
	private String commandExchange;

	@Value("${rabbitmq.crawlQueue}")
	private String crawlQueue;

	@Before
	public void setup(){
		log.info(String.format("---------> for profile %s the broker is at URI %s",
				Arrays.toString(env.getActiveProfiles()), rabbitmqURI.toString()));
	}
	
	@Test
	public void testIssueFullUpdateCommandDryRun() throws InterruptedException {
		//turn the crawler off, otherwise the messages will get consumed
		candidatesByRegionByOfficeCrawler.stop();
		
		rabbitAdmin.purgeQueue(crawlQueue, true);
		rabbitTemplate.convertAndSend(commandExchange, "tserd14.fullUpdate", "Do a full update, Scotty!");
		
		Thread.sleep(2000);
		
		assertQueueSize(crawlQueue, GeoUF.values().length * PolCargo.values().length);
	}

	@Test
	public void testIssueFullUpdateCommandWithCandidateByRegionByOfficeCrawlerRun() throws InterruptedException {
		//just let the messages get consumed
		candidatesByRegionByOfficeCrawler.start();
		
		rabbitAdmin.purgeQueue(crawlQueue, true);
		rabbitTemplate.convertAndSend(commandExchange, "tserd14.fullUpdate", "Do a full update, Scotty!");

		// adjust to a reasonable delay
		// the first trials indicate that 100000s should be enough
		// if each crawl takes 1s, and if at least 3 consumers are active
		Thread.sleep(100000);
		
		assertQueueSize(crawlQueue, 0);
	}

	
	private void assertQueueSize(String queue, Integer expectedRequestCount) {
		Properties crawlQueueProps = rabbitAdmin.getQueueProperties(queue);

		Integer actualRequestCount = (Integer)crawlQueueProps.get("QUEUE_MESSAGE_COUNT");

		assertEquals(expectedRequestCount, actualRequestCount);
	}

	
	
}
