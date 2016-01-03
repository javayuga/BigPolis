package br.net.neuromancer.bigpolis.tserd14.configurations;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

import br.net.neuromancer.bigpolis.tserd14.TSERD14Exception;
import br.net.neuromancer.bigpolis.tserd14.crawlers.CandidatesByRegionByOfficeCrawler;
import br.net.neuromancer.bigpolis.tserd14.executors.FullUpdateExecutor;

@Configuration
public class BasicConfiguration {
	
	@Bean
	public static URI rabbitmqURI(@Value("${rabbitmq.URI}") String uri) throws TSERD14Exception{
		try {
			return new URI(uri);
		} catch (URISyntaxException e) {
			throw new TSERD14Exception(e);
		}
	}
	
	@Bean
	public static FullUpdateExecutor fullUpdateOrchestrator(){
		return new FullUpdateExecutor();
	}

	@Bean
	public static CandidatesByRegionByOfficeCrawler candidatesByRegionByOfficeCrawler(){
		return new CandidatesByRegionByOfficeCrawler();
	}

	
	@Profile("dev")
	public static class DevConfig{
		@Bean
		public static PropertySourcesPlaceholderConfigurer properties() {
			return buildYamlProperties("dev-properties.yml");
		}
	}


	@Profile("prod")
	public static class ProdConfig{
		@Bean
		public static PropertySourcesPlaceholderConfigurer properties() {
			return buildYamlProperties("prod-properties.yml");
		}
	}
	
	private static PropertySourcesPlaceholderConfigurer buildYamlProperties(String resourcePath){
		PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
		YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
		yaml.setResources(new ClassPathResource("common-properties.yml"), new ClassPathResource(resourcePath));
		propertySourcesPlaceholderConfigurer.setProperties(yaml.getObject());
		return propertySourcesPlaceholderConfigurer;
	}
}
