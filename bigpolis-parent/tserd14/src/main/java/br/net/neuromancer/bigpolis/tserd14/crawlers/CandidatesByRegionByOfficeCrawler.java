package br.net.neuromancer.bigpolis.tserd14.crawlers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

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
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.gargoylesoftware.htmlunit.WebClient;

import br.net.neuromancer.bigpolis.tserd14.TSERD14Exception;
import br.net.neuromancer.bigpolis.tserd14.models.PolCandidate;
import br.net.neuromancer.bigpolis.tserd14.requests.CandidatesByRegionByOfficeCrawlRequest;

/**
 * CandidatesByRegionByOfficeCrawler
 * 
 * listens to BP_Crawl queue
 *
 */
@Component
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

	@Autowired
	protected WebClient defaultWebClient;

	@Value("${rabbitmq.crawlExchange}")
	protected String crawlExchange;

	@Value("${candidatesByRegionByOfficeCrawlers}")
	protected Integer candidatesByRegionByOfficeCrawlers;

	protected SimpleMessageListenerContainer container;

	@PostConstruct
	public void setup() throws InterruptedException {
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

	public void start() {
		if (!container.isRunning()) {
			container.start();
		}
	}

	@PreDestroy
	public void stop() {
		container.stop();
	}

	public class Listener {

		private ArrayList<PolCandidate> candidates;

		public void handleMessage(CandidatesByRegionByOfficeCrawlRequest request) throws TSERD14Exception {
			try {
				scrapCandidates(request);

				log.info(String.format("-----> found %d candidates for request %s, %s, %d", candidates.size(),
						request.getUf(), request.getCargo(), request.getCargo().ordinal() + 1));

			} catch (Exception e) {
				throw new TSERD14Exception(e);
			}

		}

		private List<PolCandidate> scrapCandidates(CandidatesByRegionByOfficeCrawlRequest request)
				throws ParserConfigurationException, MalformedURLException, SAXException, IOException,
				XPathExpressionException {
			String url = String.format(
					"http://inter01.tse.jus.br/spceweb.consulta.receitasdespesas2014/candidatoAutoComplete.do?noCandLimpo=&sgUe=%s&cdCargo=%s&orderBy=cand.NM_CANDIDATO",
					request.getUf(), request.getCargo().ordinal() + 1);

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new URL(url).openStream());

			// the terrible format of the response forces the use of XPath
			//
			XPath xpath = XPathFactory.newInstance().newXPath();

			candidates = new ArrayList<PolCandidate>();
			Integer candidateCount = Integer.parseInt(xpath.evaluate("count(//response/sqCand)", doc));

			for (int i = 1; i <= candidateCount; i++) {
				try {
					PolCandidate candidate = new PolCandidate();
					candidate.setSqCand(
							Long.parseLong(xpath.compile(String.format("//response/sqCand[%d]", i)).evaluate(doc)));
					candidate.setName(xpath.compile(String.format("//response/name[%d]", i)).evaluate(doc).trim());
					candidate.setNumero(
							Integer.parseInt(xpath.compile(String.format("//response/numero[%d]", i)).evaluate(doc)));
					candidate.setSgUe(xpath.compile(String.format("//response/sgUe[%d]", i)).evaluate(doc).trim());
					candidate
							.setPartido(xpath.compile(String.format("//response/partido[%d]", i)).evaluate(doc).trim());

					candidates.add(candidate);
				} catch (NumberFormatException e) {
					log.error(String.format("malformed number while crawling for request %s at URL %s",
							request.toString(), url));
				}
			}

			return candidates;
		}

	}

}
