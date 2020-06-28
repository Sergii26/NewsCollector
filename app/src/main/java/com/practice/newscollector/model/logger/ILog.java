package com.practice.newscollector.model.logger;

public interface ILog {
    Logger log(String message);
    void withCause(Exception cause);
}
