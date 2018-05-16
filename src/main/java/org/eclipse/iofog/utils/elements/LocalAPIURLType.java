/*
 * *******************************************************************************
 *   Copyright (c) 2018 Edgeworx, Inc.
 *
 *   This program and the accompanying materials are made available under the
 *   terms of the Eclipse Public License v. 2.0 which is available at
 *   http://www.eclipse.org/legal/epl-2.0
 *
 *   SPDX-License-Identifier: EPL-2.0
 * *******************************************************************************
 */

package org.eclipse.iofog.utils.elements;

import io.netty.handler.codec.http.HttpMethod;

public enum LocalAPIURLType {

    GET_CONFIG_REST_LOCAL_API("/v2/config/get", HttpMethod.POST),
    GET_NEXT_MSG_REST_LOCAL_API("/v2/messages/next", HttpMethod.POST),
    POST_MSG_REST_LOCAL_API("/v2/messages/new", HttpMethod.POST),
    GET_MSGS_QUERY_REST_LOCAL_API("/v2/messages/query", HttpMethod.POST),
    GET_CONTROL_WEB_SOCKET_LOCAL_API("/v2/control/socket", HttpMethod.GET),
    GET_MSG_WEB_SOCKET_LOCAL_API("/v2/message/socket", HttpMethod.GET);

    private String url;
    private HttpMethod httpMethod;

    LocalAPIURLType(String url, HttpMethod httpMethod) {
        this.url = url;
        this.httpMethod = httpMethod;
    }

    public String getURL() {
        return this.url;
    }

    public HttpMethod getHttpMethod() {
        return this.httpMethod;
    }

    public static LocalAPIURLType getByUrl(String url) {
        for (LocalAPIURLType urlType : LocalAPIURLType.values()) {
            if (urlType.getURL().equals(url)) {
                return urlType;
            }
        }
        return null;
    }

}
