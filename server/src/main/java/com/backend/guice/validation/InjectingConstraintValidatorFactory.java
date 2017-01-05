package com.backend.guice.validation;

import com.google.inject.Injector;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorFactory;

/**
 * Delegates validator creation to the delegate factory, but before returning them,
 * injects their members with guice.
 *
 * @author Jiri Zuna (jiri@zunovi.cz)
 */
public class InjectingConstraintValidatorFactory implements ConstraintValidatorFactory {
    private final Injector injector;
    private final ConstraintValidatorFactory delegate;

    public InjectingConstraintValidatorFactory(final Injector injector, final ConstraintValidatorFactory delegate) {
        this.injector = injector;
        this.delegate = delegate;
    }

    @Override
    public <T extends ConstraintValidator<?, ?>> T getInstance(final Class<T> key) {
        final T validator = delegate.getInstance(key);
        injector.injectMembers(validator);
        return validator;
    }

    @Override
    public void releaseInstance(final ConstraintValidator<?, ?> instance) {
        delegate.releaseInstance(instance);
    }
}
