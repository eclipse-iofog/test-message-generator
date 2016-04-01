package com.iotracks.utils.elements;

import io.netty.handler.codec.http.HttpMethod;

/**
 * Created by forte on 4/1/16.
 */
public enum LocalAPIURLType {

    GET_CONFIG_REST_LOCAL_API ("/v2/config/get", HttpMethod.GET) ,
    GET_NEXT_MSG_REST_LOCAL_API ("/v2/messages/next", HttpMethod.GET),
    POST_MSG_REST_LOCAL_API ("/v2/messages/new", HttpMethod.POST),
    GET_MSGS_QUERY_REST_LOCAL_API ("/v2/messages/query", HttpMethod.GET),
    GET_CONTROL_WEB_SOCKET_LOCAL_API ("/v2/control/socket/id/", HttpMethod.GET),
    GET_MSG_WEB_SOCKET_LOCAL_API ("/v2/message/socket/id/", HttpMethod.GET);

    private String url;
    private HttpMethod httpMethod;

    LocalAPIURLType(String url, HttpMethod httpMethod){
        this.url = url;
        this.httpMethod = httpMethod;
    }

    public String getURL(){
        return this.url;
    }

    public HttpMethod getHttpMethod(){
        return this.httpMethod;
    }

    public static LocalAPIURLType getByUrl(String url){
        for (LocalAPIURLType urlType : LocalAPIURLType.values()) {
            if(urlType.getURL().equals(url)){
                return urlType;
            }
        }
        return null;
    }

}
