package br.net.neuromancer.bigpolis.tserd14.configurations;

import org.springframework.context.annotation.Bean;

import com.gargoylesoftware.htmlunit.WebClient;

import br.net.neuromancer.bigpolis.tserd14.TSERD14Exception;

public class HtmlUnitConfiguration {
	@Bean
	public WebClient defaultWebClient() throws TSERD14Exception {
		return new WebClient();
	}

}
