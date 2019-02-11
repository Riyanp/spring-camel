package com.odenktools.orderservice;

import com.odenktools.common.model.Customer;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class OrderRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		restConfiguration("rest-api")
				.component("servlet")
				.bindingMode(RestBindingMode.json);

		/*rest("/student")
				.produces("application/json")
				.description("Student API")
				.get("/hello/{name}")
				.route().transform().simple("Hello ${header.name}, Welcome to JavaOutOfBounds.com")
				.endRest();

		rest("/student").produces("application/json")
				.description("Student API")
				.get("/records/{name}").to("direct:records");*/

		/*from("direct:records")
				.to("http://localhost:6000/api/v1/customer?id=9c728ecd-23da-44cd-8e77-a29781c2a3e7");*/

		//Random amount = new Random();
		//order.setAmount(amount.nextInt(10) + 1);

		/*from("direct:records")
				.to("log:records?level=INFO&showAll=true&multiline=true")
				.process(new Processor() {
					final AtomicLong counter = new AtomicLong();
					@Override
					public void process(Exchange exchange) throws Exception {

						final String name = exchange.getIn().getHeader("name", String.class);
						log.error(name);

						exchange.getIn().setBody(Customer.builder()
								.id(counter.toString())
								.username("sssssssss")
								.email("moeloet@gmail.com")
								.fullName("moeloet odenktools")
								.isActive(1)
								.isDeleted(0)
								.amountBalanced(new BigDecimal(BigInteger.ZERO))
								.phoneNumber("02020202002")
								.build());
					}
				});*/

		/*onException(JsonProcessingException.class)
				.handled(true)
				.to("log:" + OrderRoute.class.getName() + "?showAll=true&multiline=true&level=ERROR")
				.removeHeaders("*", "")
				.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
				.bean("restbuilder", "errorResponse(4000,'Invalid json content')");*/

		//from("file:C://inputFolder?noop=true").to("file:C://outputFolder");

		/*from(ServiceConstants.HELLO_SERVICE_ENDPOINT)
				.id(ServiceConstants.HELLO_ROUTE_ID)
				.log("I'm in the Camel Route!")
				.process(new HelloProcessor());*/

		/*rest("/user").description("User API")
				.produces("application/json").consumes("application/json")
				.skipBindingOnErrorCode(false) //Enable json marshalling for body in case of errors
				.get("/{id}")
				.description("Query user")
				//route
				.route().routeId("user-get")
				.bean("routeHelper", "logHeadersByPattern")
				.endRest()
		.post();*/
	}

}