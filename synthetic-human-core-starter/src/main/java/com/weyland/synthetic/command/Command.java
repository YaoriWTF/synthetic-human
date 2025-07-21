package com.weyland.synthetic.command;

import java.time.Instant;

public interface Command<T> {
    CommandResult<T> execute();
    CommandMetadata getMetadata();
}

