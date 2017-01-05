package com.backend.guice.validation;

import com.google.inject.Injector;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.validation.Configuration;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.Validation;
import javax.validation.Validator;

/**
 * Creates a validator with our implementation of injecting validator factory.
 * We can use @Inject in our validators.
 *
 * @author Jiri Zuna (jiri@zunovi.cz)
 */
public class ValidatorProvider implements Provider<Validator> {

    private final Injector injector;

    @Inject
    public ValidatorProvider(final Injector injector) {
        this.injector = injector;
    }

    @Override
    public Validator get() {
        final Configuration<?> valCfg = Validation.byDefaultProvider().configure();
        final ConstraintValidatorFactory defaultValidationFactory = valCfg.getDefaultConstraintValidatorFactory();
        Validator validator = valCfg.
                constraintValidatorFactory(new InjectingConstraintValidatorFactory(injector, defaultValidationFactory)).
                buildValidatorFactory().
                getValidator();
        return validator;
    }
}
