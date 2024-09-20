package com.example.jabaJuiceServer.config;
import com.example.jabaJuiceServer.services.UserUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    @Bean
    public UserDetailsService userDetailsService(){
        return new UserUserDetailsService();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
         http
                .authorizeHttpRequests()
                .requestMatchers("/sanny/products/new","/stori-ya-jaba-items/video/**","/**",
                        "/jaba/users/new","/storis/**","/profile_images/**","/product_images/**","/jaba/users/byEmail/**",
                        "jaba/users/**","/jaba/users/unfollow/byFileName","/sanny/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf(AbstractHttpConfigurer :: disable)
                 .authenticationProvider(authenticationProvider())
                .httpBasic(Customizer.withDefaults());

//        .requestMatchers("api/products/**","/stori-ya-jaba-items/**",
//                "jaba/users/**")
        return http.build();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // other configuration beans and methods...
}





