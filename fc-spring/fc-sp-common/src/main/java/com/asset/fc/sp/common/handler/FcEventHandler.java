package com.asset.fc.sp.common.handler;

import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

/**
 *
 * @author nour.ihab
 */
public abstract class FcEventHandler {

    @EventListener
    public abstract void start(ContextRefreshedEvent start);

    @EventListener
    public final void Shutdown(ContextClosedEvent close) {
        preShutdown();
    }

    public abstract void preShutdown();

}
