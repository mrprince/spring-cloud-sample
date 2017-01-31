package com.example;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.amqp.Amqp;
import org.springframework.integration.dsl.http.Http;
import org.springframework.integration.splitter.DefaultMessageSplitter;

import java.io.IOException;

@SpringBootApplication
@IntegrationComponentScan
public class IntegrationServerApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(IntegrationServerApplication.class, args);
	}
//	@Bean
//	public IntegrationFlow flow(){
//		return IntegrationFlows.from(Http.inboundGateway("/receiveGateway")
//				.requestMapping(m -> m.methods(HttpMethod.POST))
//				.requestPayloadType(String.class))
//				.split(commaSplitter())
//				.<String, String>transform(p -> p + " from the other side")
//                .<String, String>transform(String::toUpperCase)
//				.aggregate()
//				.transform(Object::toString)
//				.get();
//	}

	@Bean
	public IntegrationFlow flow(RabbitTemplate rabbitTemplate){
		return IntegrationFlows.from(Http.inboundGateway("/receiveGateway")
				.requestMapping(m -> m.methods(HttpMethod.POST))
				.requestPayloadType(String.class))
				.split(commaSplitter())
				.<String, String>transform(p -> p + " from the other side")
				.handle(Amqp.outboundGateway(rabbitTemplate).routingKey("foo"))
				.aggregate()
				.transform(Object::toString)
				.get();
	}

	@Bean
	public IntegrationFlow amqp(ConnectionFactory connectionFactory){
		return IntegrationFlows.from(Amqp.inboundGateway(connectionFactory, "foo"))
                .<String, String>transform(String::toUpperCase)
				.get();
	}


	@Bean
	DefaultMessageSplitter commaSplitter(){
		DefaultMessageSplitter splitter = new DefaultMessageSplitter();
		splitter.setDelimiters(",");
		return splitter;
	}
}
