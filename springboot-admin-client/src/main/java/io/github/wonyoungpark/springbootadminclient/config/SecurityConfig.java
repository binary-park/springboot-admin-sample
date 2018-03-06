package io.github.wonyoungpark.springbootadminclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Created by wyparks2@gmail.com on 2018. 3. 6.
 * Blog : http://WonYoungPark.github.io
 * Github : http://github.com/WonYoungPark
 * config
 */
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http.authorizeExchange().anyExchange().permitAll()
                   .and().csrf().disable()
                   .build();
    }
}
