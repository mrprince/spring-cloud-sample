package com.example.security;

import org.audit4j.core.AuditManager;
import org.audit4j.core.dto.EventBuilder;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class LoginListener implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {

    @Override
    public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event)
    {
        AuditManager.getInstance().audit(new EventBuilder().addAction("login").build());
    }
}