package com.example;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
@EnableBinding(Source.class)
@EnableFeignClients
public class ReservationClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservationClientApplication.class, args);
	}

	@Bean
    @LoadBalanced
    RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    AlwaysSampler alwaysSampler(){
        return new AlwaysSampler();
    }
}

@RestController
@RequestMapping("/reservations")
class ReservationApiGateWayRestController{
    @Autowired
    @LoadBalanced
    private RestTemplate restTemplate;

    @Output(Source.OUTPUT)
    @Autowired
    private MessageChannel messageChannel;

    @Autowired
    ReservationService reservationService;

    @RequestMapping(method= RequestMethod.POST)
    public void write(@RequestBody Reservation r){
        this.messageChannel.send(MessageBuilder.withPayload(r.getReservationName()).build());
    }

    public Collection<String> getReservationNamesFallBack(){
        return Collections.emptyList();
    }

    @RequestMapping("/names")
    @HystrixCommand(fallbackMethod = "getReservationNamesFallBack")
    public Collection<String> getReservationNames(){

        ParameterizedTypeReference<Resources<Reservation>> ptr =
                new ParameterizedTypeReference<Resources<Reservation>>() {
                };

        ResponseEntity<Resources<Reservation>> responseEntity =
                this.restTemplate.exchange("http://reservation-service/reservations",
                        HttpMethod.GET,
                        null,
                        ptr
                );

        return responseEntity
                .getBody()
                .getContent()
                .stream()
                .map(Reservation::getReservationName)
                .collect(Collectors.toList());

    }

    @RequestMapping("/message")
    public String message(){
        return reservationService.message();
    }
}

@FeignClient("reservation-service")
interface ReservationService {
    @RequestMapping(method = RequestMethod.GET, value = "/message")
    String message();
}

@Data
class Reservation{
    private long Id;
    private String reservationName;
}
