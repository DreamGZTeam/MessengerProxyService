package com.bftcom.ws;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import javax.xml.ws.Endpoint;

/**
 * Created by Artem on 25.09.2016.
 */
@SpringBootApplication
public class WebServiceConfiguration extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(WebServiceConfiguration.class, args);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(WebServiceConfiguration.class);
  }

  @Bean
  public ServletRegistrationBean dispatcherServlet() {
    return new ServletRegistrationBean(new CXFServlet());
  }

  @Bean(name= Bus.DEFAULT_BUS_ID)
  public SpringBus springBus() {
    return new SpringBus();
  }

  @Bean
  public MessengerProxyService messengerProxyService() {
    return new MessengerProxyServiceImpl();
  }

  @Bean
  public Endpoint endpoint() {
    EndpointImpl endpoint = new EndpointImpl(springBus(), messengerProxyService());
    endpoint.publish("/MessengerProxyService");
    return endpoint;
  }
}
