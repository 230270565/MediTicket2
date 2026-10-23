/*
 JacksonConfig.java

 Jackson setup for reading and writing dates as JSON

 Authors:
  Joshua Reid Adams (230317693)
  Abdullahi Raage Farah (230971091)
  Aidan Barends (230255639)
  Jaden Clayton Abrahams (222206721)
  Joshua Peter Bonzet (221312536)
  Matthew Barron (230398863)
  Raul Jaaim Everts (230270565)

 Date: 15th August 2026
*/

package za.ac.cput.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Our domain classes use builders and have no setters, so Jackson reads the fields directly.
// Uses the Jackson 3 API that comes with Spring Boot 4.
@Configuration
public class JacksonConfig {

    @Bean
    public JsonMapperBuilderCustomizer jacksonFieldVisibilityCustomizer() {
        return builder -> builder.changeDefaultVisibility(
                visibilityChecker -> visibilityChecker.withFieldVisibility(JsonAutoDetect.Visibility.ANY)
        );
    }
}
