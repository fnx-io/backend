package com.backend.util;

import java.nio.charset.Charset;
import java.util.Locale;

/**
 * Place to store useful constants
 */
public final class Constants {

    public static final Locale LOCALE = Locale.US;

    /**
     * We prefer UTF-8 encoding.
     */
    public static final String ENCODING = "UTF-8";

    public static final Charset CHARSET = Charset.forName(ENCODING);

    // unprocessable entity
    public static final int STATUS_VALIDATION_FAILED = 422;

    public static final String HEADER_FILENAME = "X-Filename";
}
