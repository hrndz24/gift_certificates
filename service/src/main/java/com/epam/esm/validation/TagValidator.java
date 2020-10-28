package com.epam.esm.validation;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ValidatorException;
import org.springframework.stereotype.Component;

@Component
public class TagValidator {

    public void validate(Tag tag) throws ValidatorException {
        checkNonNull(tag);
        validateName(tag.getName());
    }

    private void validateName(String name) throws ValidatorException {
        if (name == null) {
            throw new ValidatorException("Null tag name");
        }
        if (name.isEmpty()) {
            throw new ValidatorException("Empty tag name");
        }
    }

    private void checkNonNull(Tag tag) throws ValidatorException {
        if (tag == null) {
            throw new ValidatorException("Null tag");
        }
    }
}