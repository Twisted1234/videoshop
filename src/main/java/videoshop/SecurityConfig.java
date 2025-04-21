package videoshop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService) {
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Bean
    SecurityFilterChain videoShopSecurity(HttpSecurity http) throws Exception {
        http
                // H2-Console o.ä.
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                // CSRF abschalten, wenn du es für APIs nicht nutzen willst
                .csrf(csrf -> csrf.disable())
                // URL-basierte Autorisierung
                .authorizeHttpRequests(authorize -> authorize
                        // Login-Seite und statische Ressourcen freigeben
                        .requestMatchers(
                                "/login",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/oauth2/**"        // auch OAuth2‑Redirects freigeben
                        ).permitAll()
                        // Warenkorb schützen
                        .requestMatchers("/cart/**").authenticated()
                        // Rest für alle frei verfügbar
                        .anyRequest().permitAll()
                )
                // Klassisches Login‑Formular
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")  // der POST-Endpunkt
                        .permitAll()
                )
                // OAuth2‑Login (Google, Facebook etc.)
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")          // gleiche Login-Seite
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                )
                // Logout konfigurieren
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        return http.build();
    }
}

