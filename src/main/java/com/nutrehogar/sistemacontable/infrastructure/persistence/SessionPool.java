package com.nutrehogar.sistemacontable.infrastructure.persistence;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class SessionPool {
    private final BlockingQueue<Session> sessionQueue;
    private final SessionFactory sessionFactory;
    private final int maxPoolSize;

    public SessionPool(SessionFactory sessionFactory, int maxPoolSize) {
        this.sessionFactory = sessionFactory;
        this.maxPoolSize = maxPoolSize;
        this.sessionQueue = new LinkedBlockingQueue<>(maxPoolSize);
        initializePool();
        log.info("SessionPool initialized");
        log.info("SessionPool - maxPoolSize: {}", maxPoolSize);
    }

    private void initializePool() {
        for (int i = 0; i < maxPoolSize; i++) {
            sessionQueue.add(sessionFactory.openSession());
        }
    }

    public Session borrowSession() throws InterruptedException {
        return sessionQueue.take();
    }

    public void returnSession(Session session) {
        if (session.isOpen()) {
            sessionQueue.offer(session);
        }
    }

    public void shutdown() {
        sessionQueue.forEach(Session::close);
        sessionQueue.clear();
    }
}