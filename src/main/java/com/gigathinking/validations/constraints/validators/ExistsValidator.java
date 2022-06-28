package com.gigathinking.validations.constraints.validators;

import com.gigathinking.validations.constraints.Exists;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ExistsValidator implements ConstraintValidator<Exists, Long> {
    private String table;

    @Inject
    EntityManager entityManager;

    @Override
    public void initialize(Exists constraintAnnotation) {
        this.table = constraintAnnotation.table();
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        Query query = entityManager.createNativeQuery("select count(*) from " + this.table + " where id = " + value.toString());
        return Integer.parseInt(query.getSingleResult().toString()) > 0;
    }
}
