package br.net.neuromancer.bigpolis.tserd14.crawlers;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import br.net.neuromancer.bigpolis.tserd14.requests.CandidatesByRegionByOfficeCrawlRequest;

/**
 * CandidatesByRegionByOfficeCrawler
 * 
 * listens to BP_Crawl queue
 *
 */
public class CandidatesByRegionByOfficeCrawler {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	protected ConnectionFactory connectionFactory;

	@Autowired
	protected RabbitTemplate rabbitTemplate;
	
	@Autowired 
	protected RabbitAdmin rabbitAdmin;
	
	@Value("${rabbitmq.crawlQueue}")
	protected String crawlQueue;
	
	@Value("${rabbitmq.crawlExchange}")
	protected String crawlExchange;

	@Value("${candidatesByRegionByOfficeCrawlers}")
	protected Integer candidatesByRegionByOfficeCrawlers;

	
	protected SimpleMessageListenerContainer container;
	
	@PostConstruct
	public void setup() throws InterruptedException{
		// set up the queue, exchange, binding on the broker
		Queue queue = new Queue(crawlQueue);
		rabbitAdmin.declareQueue(queue);
		
		DirectExchange exchange = new DirectExchange(crawlExchange);
		rabbitAdmin.declareExchange(exchange);
		rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange).with("tserd14.candidatesByRegionByOffice"));
		
		container = new SimpleMessageListenerContainer(connectionFactory);
		
		MessageListenerAdapter adapter = new MessageListenerAdapter(new Listener());
		container.setConcurrentConsumers(candidatesByRegionByOfficeCrawlers);
		container.setMessageListener(adapter);
		container.setQueueNames(crawlQueue);
		
		start();
	
	}

	public void start(){
		if (!container.isRunning()){
			container.start();
		}
	}
	
	@PreDestroy
	public void stop(){
		container.stop();
	}
	
	
	
	public class Listener{
		public void handleMessage(CandidatesByRegionByOfficeCrawlRequest request) throws InterruptedException {
			log.info(String.format("-----> crawling candidates for %s ", request.toString()));
			Thread.sleep(1000);
		}
	}
	
}
