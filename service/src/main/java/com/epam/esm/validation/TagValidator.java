package com.epam.esm.validation;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ValidatorException;
import org.springframework.stereotype.Component;

@Component
public class TagValidator {

    public void validate(Tag tag){
        checkNonNull(tag);
        validateName(tag.getName());
    }

    private void validateName(String name) {
        if (name == null) {
            throw new ValidatorException("Null tag name");
        }
        if (name.isEmpty()) {
            throw new ValidatorException("Empty tag name");
        }
    }

    private void checkNonNull(Tag tag){
        if (tag == null) {
            throw new ValidatorException("Null tag");
        }
    }
}