package org.example;

import java.util.HashMap;
import java.util.Map;

public class HttpRequestData {

    public String method;
    public String path;
    public String version;

    public Map<String,String> headers = new HashMap<>();
    public String body;

}
