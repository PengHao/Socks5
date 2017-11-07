package com.wolfpeng.core.http;
import java.util.HashMap;
import java.util.Map;

import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion.Static;

/**
 * Created by penghao on 2017/1/22.
 */
public class HttpHead {
    //request
    public final static String ACCEPT = "Accept";
    public final static String ACCEPT_CHARSET = "Accept-Charset";
    public final static String ACCEPT_ENCODING = "Accept-Encoding";
    public final static String ACCEPT_LANGUAGE= "Accept-Language";
    public final static String ACCEPT_RANGE= "Accept-Ranges";
    public final static String AUTHORIZATION ="Authorization";
    public final static String CACHE_CONTROL= "Cache-Control";
    public final static String CONNECTION = "Connection";
    public final static String COOKIE = "Cookie";
    public final static String CONTENT_LENGTH = "Content-Length";
    public final static String CONTENT_TYPE = "Content-Type";
    public final static String DATE = "Date";
    public final static String EXPECT = "Expect";
    public final static String FROM = "From";
    public final static String HOST = "Host";
    public final static String IF_MATCH = "If-Match";
    public final static String IF_MODIFIED_SINCE = "If-Modified-Since";
    public final static String IF_NONE_MATCH = "If-None-Match";
    public final static String IF_RANGE = "If-Range";
    public final static String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
    public final static String IF_MAX_FORWARD = "Max-Forwards";
    public final static String PRAGAM = "Pragma";
    public final static String PROXY_AUTHORIZATION = "RemouteHttpConnectionHandle-Authorization";
    public final static String RANGE = "Range";
    public final static String REFERER = "Referer";
    public final static String TE = "TE";
    public final static String UPGRADE = "Upgrade";
    public final static String USER_AGENT = "Task-Agent";
    public final static String VIA = "Via";
    public final static String WARNING = "Warning";

    //response
    public final static String AGE = "Age";
    public final static String ALLOW = "Allow";
    public final static String CONTENT_ENCODING = "Content-Encoding";
    public final static String CONTENT_LANGUAGE = "Content-Language";
    public final static String CONTENT_LOCATION = "Content-Location";
    public final static String CONTENT_MD5 = "Content-MD5";
    public final static String CONTENT_RANGE = "Content-Range";
    public final static String ETAG = "Etag";
    public final static String EXPIRES = "Expires";
    public final static String LAST_MODIFIED = "Last-Modified";
    public final static String LOCATION = "Location";
    public final static String PROXY_AUTHENTICATE = "RemouteHttpConnectionHandle-Authenticate";
    public final static String REFRESH = "refresh";
    public final static String RETRY_AFTER = "Retry-After";
    public final static String SERVER = "Server";
    public final static String SET_COOKIE = "Set-Cookie";
    public final static String TRAILER = "Trailer";
    public final static String TRANSFER_ENCODING = "Transfer-Encoding";
    public final static String VARY = "Vary";
    public final static String WWW_AUTHENTICATE = "WWW-Authenticate";

    public String method, path;
    HashMap<String, String> get_params;
    HashMap<String, String> headerParams;
    public HashMap<String, String> getHeaderParams() {
        return headerParams;
    }


    int headerLength = 0;

    public HttpHead(String headerString) {
        headerParams = new HashMap<String, String>();
        get_params = new HashMap<String, String>();
        for (String param: headerString.split("\\r\\n")) {
            String[] kv = param.split(": ");
            if (kv.length>1) {
                headerParams.put(kv[0], kv[1]);
            } else {
                headerParams.put("first", kv[0]);
                praserFirst(kv[0]);
            }
        }
        headerLength = headerString.length();
    }

    private void praserFirst(String first) {
        for (String param: first.split(" ")) {
            if (method == null) {
                method = param;
            } else  {
                String[] ps = param.split("\\?");
                if (ps.length > 1) {
                    path = ps[0];
                    for (String p: ps[1].split("&")) {
                        String[] kv = p.split("=");
                        if (kv.length>1) {
                            get_params.put(kv[0], kv[1]);
                        }
                    }
                }
            }
        }
    }

    static public String getCookie(String cookieString, String cookieFeild) {
        return getCookieMap(cookieString).get(cookieFeild);
    }

    static public HashMap<String, String> getCookieMap(String cookieString) {
        HashMap<String, String> cookie = new HashMap<String, String>();
        if (cookieString == null) {
            return cookie;
        }
        for (String subCoockie : cookieString.split(";")) {
            String[] kv = subCoockie.trim().split("=");
            if (kv.length > 1) {
                cookie.put(kv[0], kv[1]);
            }
        }
        return cookie;
    }



    public String getGetProperty(String property) {
        return get_params.get(property);
    }
    public void setGetProperty(String property, String value) {
        get_params.put(property, value);
    }

    public String getHeaderProperty(String property) {
        return headerParams.get(property);
    }

    public void setHeaderProperty(String property, String value) {
        headerParams.put(property, value);
    }
}
