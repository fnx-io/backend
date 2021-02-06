package io.fnx.backend.web;

import com.google.inject.Singleton;
import org.mint42.annotations.On;

import javax.servlet.http.HttpServletResponse;

@Singleton
public class HeadersDecorator {

    @On(stage = On.Stage.INIT)
    public void addHeaders(HttpServletResponse resp) {
        resp.addHeader("X-Frame-Options", "DENY");
    }
}
