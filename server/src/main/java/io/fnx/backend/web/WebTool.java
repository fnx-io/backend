package io.fnx.backend.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Nástroj pro různé jednoduché webové operace, jako např. práce s cookies.
 *
 * @author Tomas Zverina, zverina@m-atelier.cz
 */
public final class WebTool {

    // no instances
    private WebTool() { }

    public static String getCookieValue(HttpServletRequest httpServletRequest, String name) {
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies == null || cookies.length == 0) return null;
        for (int i = 0; i < cookies.length; i++) {
            Cookie cookie = cookies[i];
            if (name.equals(cookie.getName())) return cookie.getValue();
        }
        return null;
    }

    /**
     * Nastavi cookie na 30 dni.
     *
     * @param httpServletResponse
     * @param name
     * @param value
     */
    public static void setCookieValue(HttpServletResponse httpServletResponse, String name, String value, int days) {
        Cookie c = new Cookie(name, value);
        c.setPath("/");
        c.setMaxAge(days*24*60*60);
        httpServletResponse.addCookie(c);
    }

    /**
     * Smaze cookie.
     *
     * @param httpServletResponse
     * @param cookieName
     */
    public static void deleteCookie(HttpServletResponse httpServletResponse, String cookieName) {
        Cookie c = new Cookie(cookieName, null);
        c.setPath("/");
        c.setMaxAge(-1);
        httpServletResponse.addCookie(c);
    }

}
