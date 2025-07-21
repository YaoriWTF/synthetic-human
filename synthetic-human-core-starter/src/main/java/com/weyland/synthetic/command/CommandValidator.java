package com.weyland.synthetic.command;

import com.weyland.synthetic.exception.SyntheticHumanException;
import org.springframework.stereotype.Component;

@Component
public class CommandValidator {

    public void validate(CommandMetadata metadata) throws SyntheticHumanException {
        if (metadata.description() == null || metadata.description().length() > 1000) {
            throw new SyntheticHumanException("Description must be not null and up to 1000 characters");
        }

        if (metadata.priority() == null) {
            throw new SyntheticHumanException("Priority must be specified");
        }

        if (metadata.author() == null || metadata.author().length() > 100) {
            throw new SyntheticHumanException("Author must be not null and up to 100 characters");
        }

        if (metadata.time() == null) {
            throw new SyntheticHumanException("Time must be specified");
        }
    }
}
