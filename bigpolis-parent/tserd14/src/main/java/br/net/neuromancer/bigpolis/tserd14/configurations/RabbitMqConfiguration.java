package br.net.neuromancer.bigpolis.tserd14.configurations;

import java.net.URI;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import br.net.neuromancer.bigpolis.tserd14.TSERD14Exception;

public class RabbitMqConfiguration {

	@Autowired
	protected URI rabbitmqURI;

	@Bean
	public ConnectionFactory connectionFactory() {
		return new CachingConnectionFactory(rabbitmqURI);
	}

	@Bean
	public RabbitTemplate rabbitTemplate() throws TSERD14Exception {
		return new RabbitTemplate(connectionFactory());
	}

	@Bean
	public RabbitAdmin rabbitAdmin() throws TSERD14Exception {
		return new RabbitAdmin(connectionFactory());
	}

	
}
