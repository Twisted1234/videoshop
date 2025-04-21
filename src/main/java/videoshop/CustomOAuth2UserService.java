package videoshop;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User user = super.loadUser(userRequest);
        // Extrahiere die benötigten Informationen aus dem Google-OAuth2User
        String email = user.getAttribute("email");
        // Prüfe, ob der Benutzer in deiner Datenbank existiert. Falls nicht, registriere ihn.
        // Hier implementierst du deine Logik, z.B.:
        // - Automatische Registrierung
        // - Weiterleitung auf einen Konfigurations-/Registrierungsseite, wenn zusätzliche Daten benötigt werden
        return user;
    }
}
