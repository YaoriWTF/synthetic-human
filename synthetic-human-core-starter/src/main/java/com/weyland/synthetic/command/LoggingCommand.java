package com.weyland.synthetic.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingCommand implements Command<String> {
    private final CommandMetadata metadata;
    private final Logger logger = LoggerFactory.getLogger(LoggingCommand.class);

    public LoggingCommand(CommandMetadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public CommandResult<String> execute() {
        logger.info("Executing command: {}", metadata.description());
        logger.info("Command details - Priority: {}, Author: {}, Time: {}",
                metadata.priority(), metadata.author(), metadata.time());

        return new CommandResult<>(
                "Command executed successfully",
                CommandResult.Status.SUCCESS,
                "Success"
        );
    }

    @Override
    public CommandMetadata getMetadata() {
        return metadata;
    }
}
