package de.hfu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.rcp.RemoteAuthenticationManager;
import org.springframework.security.authentication.rcp.RemoteAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
            .antMatchers("/messageForm.html").hasRole("USER")
            .and()
        .formLogin()
            .loginPage("/login.html")
            .loginProcessingUrl("/security/spring_security_check")
            .defaultSuccessUrl("/messages.html")
            .permitAll()
            .and()
        .logout()
            .logoutSuccessUrl("/messages.html")
            .logoutUrl("/security/spring_security_logout")
            .permitAll()
            .and()
        .sessionManagement()
            .invalidSessionUrl("/messages.html")
            .and()
         .csrf()
            .disable();
  }


  @Autowired
  public RemoteAuthenticationManager remoteAuthenticationManager;

  @Bean
  public RemoteAuthenticationProvider remoteAuthenticationProvider() {
    RemoteAuthenticationProvider provider = new RemoteAuthenticationProvider();
    provider.setRemoteAuthenticationManager(remoteAuthenticationManager);
    return provider;
  }

  @Autowired
  public void registerSharedAuthentication(AuthenticationManagerBuilder auth) throws Exception {
    auth.eraseCredentials(false).authenticationProvider(remoteAuthenticationProvider());
  }


}
