package br.net.neuromancer.bigpolis.tserd14.executors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import br.net.neuromancer.bigpolis.tserd14.models.GeoUF;
import br.net.neuromancer.bigpolis.tserd14.models.PolCargo;
import br.net.neuromancer.bigpolis.tserd14.requests.CandidatesByRegionByOfficeCrawlRequest;

/**
 * FullUpdateOrchestrator
 * 
 * listens to BP_Commands queue issues requests for bulk updates of Candidate
 * basic info based on Region (geoUF) and Office (polCargo)
 *
 */
public class FullUpdateExecutor {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	protected ConnectionFactory connectionFactory;

	@Autowired
	protected RabbitTemplate rabbitTemplate;

	@Autowired
	protected RabbitAdmin rabbitAdmin;

	@Value("${rabbitmq.commandQueue}")
	protected String commandQueue;

	@Value("${rabbitmq.commandExchange}")
	protected String commandExchange;

	@Value("${rabbitmq.crawlExchange}")
	protected String crawlExchange;

	private SimpleMessageListenerContainer container;

	@PostConstruct
	public void setup() throws InterruptedException {
		// set up the queue, exchange, binding on the broker
		Queue queue = new Queue(commandQueue);
		rabbitAdmin.declareQueue(queue);
		
		DirectExchange exchange = new DirectExchange(commandExchange);
		rabbitAdmin.declareExchange(exchange);
		rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange).with("tserd14.fullUpdate"));

		container = new SimpleMessageListenerContainer(connectionFactory);
		container.setQueueNames(commandQueue);
		container.setAcknowledgeMode(AcknowledgeMode.AUTO);

		MessageListenerAdapter adapter = new MessageListenerAdapter(new Listener());
		container.setMessageListener(adapter);
		container.start();

	}

	@PreDestroy
	public void stop() {
		container.stop();
	}

	public class Listener {
		public void handleMessage(String foo) {
			for (PolCargo cargo : PolCargo.values()) {
				for (GeoUF uf : GeoUF.values()) {
					rabbitTemplate.convertAndSend(crawlExchange, "tserd14.candidatesByRegionByOffice",
							new CandidatesByRegionByOfficeCrawlRequest(uf, cargo));
				}
			}
		}

	}

}
