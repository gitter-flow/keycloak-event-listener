package com.gitter.keycloaklistener;

import org.jboss.logging.Logger;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RealmProvider;
import org.keycloak.models.UserModel;

import java.io.IOException;

public class SocialApiEventListenerProvider implements EventListenerProvider {

    private static final Logger log = Logger.getLogger(SocialApiEventListenerProvider.class);

    private final KeycloakSession session;
    private final RealmProvider model;

    public SocialApiEventListenerProvider(KeycloakSession session) {
        this.session = session;
        this.model = session.realms();
    }

    @Override
    public void onEvent(Event event) {
        log.infof("## NEW %s EVENT", event.getType());
        log.info("-----------------------------------------------------------");
        if (EventType.REGISTER.equals(event.getType())) {
            log.info("## NEW REGISTER EVENT");
            
            RealmModel realm = this.model.getRealm(event.getRealmId());
            UserModel newRegisteredUser = this.session.users().getUserById(event.getUserId(), realm);
            try {
                UserHttpRequest.sendUserRequest(newRegisteredUser);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }

            log.info("-----------------------------------------------------------");
        }

    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean b) {

    }

    @Override
    public void close() {

    }
}
