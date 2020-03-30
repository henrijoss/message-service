package de.hfu;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.security.authentication.rcp.RemoteAuthenticationManager;
import org.springframework.security.remoting.httpinvoker.AuthenticationSimpleHttpInvokerRequestExecutor;

import de.hfu.messages.domain.service.MessageService;
import de.hfu.messages.domain.service.SecurityService;

@Configuration
@ConfigurationProperties(prefix="remoting")
public class RemotingConfiguration {

  private String serviceHost;

  @Bean
  HttpInvokerProxyFactoryBean messageService() {
    HttpInvokerProxyFactoryBean invoker = new HttpInvokerProxyFactoryBean();
    invoker.setServiceUrl(getServiceHost() + "/message.service");
    invoker.setServiceInterface(MessageService.class);
    invoker.setHttpInvokerRequestExecutor(httpInvokerRequestExecutor());
    return invoker;
  }

  @Bean
  HttpInvokerProxyFactoryBean securityService() {
    HttpInvokerProxyFactoryBean invoker = new HttpInvokerProxyFactoryBean();
    invoker.setServiceUrl(getServiceHost() + "/security.service");
    invoker.setServiceInterface(SecurityService.class);
    invoker.setHttpInvokerRequestExecutor(httpInvokerRequestExecutor());
    return invoker;
  }


  // Returning HttpInvokerProxyFactoryBean yields a autowired error in SecurityConfig.
  // Spring doesn't seem to find the serviceInterface of the proxy factory
  @Bean
  RemoteAuthenticationManager remoteAuthenticationManager() {
    HttpInvokerProxyFactoryBean invoker = new HttpInvokerProxyFactoryBean();
    invoker.setServiceUrl(getServiceHost() + "/authentication.service");
    invoker.setServiceInterface(RemoteAuthenticationManager.class);
    invoker.afterPropertiesSet();
    return (RemoteAuthenticationManager) invoker.getObject();
  }


  @Bean
  public AuthenticationSimpleHttpInvokerRequestExecutor httpInvokerRequestExecutor() {
    return new AuthenticationSimpleHttpInvokerRequestExecutor();
  }

  public String getServiceHost() {
    return serviceHost;
  }

  public void setServiceHost(String serviceHost) {
    this.serviceHost = serviceHost;
  }

}
