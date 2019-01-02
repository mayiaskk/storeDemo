package com.mayihi.store.Util;

import javax.servlet.http.Cookie;

public class CookieUtils {

    public static Cookie getCookieByName(Cookie[] cookies, String name) {
        if(cookies == null) return null;
        for (Cookie c : cookies) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }

}
