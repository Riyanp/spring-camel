package com.odenktools.customerservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Bootstap Apps.
 *
 * @author Odenktools.
 */
//@EnableDiscoveryClient
@SpringBootApplication
@EntityScan("com.odenktools.common.model")
public class CustomerServiceApplication {

	private static final Logger LOG = LoggerFactory.getLogger(CustomerServiceApplication.class);

	public static void main(String[] args) {

		SpringApplication.run(CustomerServiceApplication.class, args);
		LOG.info("APPLICATION IS RUNNING!");
	}

	/*@Bean
	public ServletRegistrationBean camelServletRegistrationBean() {
		ServletRegistrationBean registration = new ServletRegistrationBean(
				new CamelHttpTransportServlet(), "/camel/*");
		registration.setName("CamelServlet");
		return registration;
	}*/

	/*@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(10);
	}*/
}
