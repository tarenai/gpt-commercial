package com.warape.aimechanician.config;


import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.LegacyCookieProcessor;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author apeto
 * @create 2023/3/27 11:24
 */
@Slf4j
@Configuration
public class BeanConfig {

//  @Autowired
//  private ChatConfigProperties chatConfigProperties;

//  @Bean
//  public SpringUtil springUtil () {
//    return new SpringUtil();
//  }

  @Bean
  public ConfigurableServletWebServerFactory webServerFactory () {
    TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
    factory.addConnectorCustomizers(connector -> connector.setProperty("relaxedQueryChars", "|{}[]\\"));
    return factory;
  }

  @Bean
  public WebServerFactoryCustomizer<TomcatServletWebServerFactory> cookieProcessorCustomizer() {
    return tomcatServletWebServerFactory -> tomcatServletWebServerFactory.addContextCustomizers(context -> {
      context.setCookieProcessor(new LegacyCookieProcessor());
    });
  }


}
