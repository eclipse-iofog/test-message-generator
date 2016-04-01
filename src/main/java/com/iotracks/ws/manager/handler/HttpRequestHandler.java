package com.iotracks.ws.manager.handler;

import com.iotracks.utils.elements.LocalAPIURLType;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.*;

import javax.json.*;
import java.io.StringReader;
import java.util.List;
import java.util.concurrent.Callable;

import static io.netty.handler.codec.http.HttpMethod.POST;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by forte on 4/1/16.
 */
public class HttpRequestHandler implements Callable {

    private final FullHttpRequest req;
    private ByteBuf bytesData;
    private LocalAPIURLType urlType;

    public HttpRequestHandler(FullHttpRequest req, ByteBuf bytesData, LocalAPIURLType urlType){
        this.req = req;
        this.bytesData = bytesData;
        this.urlType = urlType;
    }

    @Override
    public Object call() throws Exception {
        HttpHeaders headers = req.headers();

        if (req.getMethod() != urlType.getHttpMethod()) {
            // log incorrect http method
            return new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.METHOD_NOT_ALLOWED);
        }

        if(!(headers.get(HttpHeaders.Names.CONTENT_TYPE).equals("application/json"))){
            // log incorrect type header
            String errorMsg = " Incorrect content type ";
            bytesData.writeBytes(errorMsg.getBytes());
            return new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.BAD_REQUEST, bytesData);
        }


        switch (urlType) {
            case GET_CONFIG_REST_LOCAL_API:
                // handle;
                break;
            case GET_NEXT_MSG_REST_LOCAL_API:
                // handle;
                break;
            case POST_MSG_REST_LOCAL_API:
                // handle;
                break;
            case GET_MSGS_QUERY_REST_LOCAL_API:
                // handle;
                break;
        }

        // bytesData.writeBytes(responseData);
        FullHttpResponse res = new DefaultFullHttpResponse(HTTP_1_1, OK, bytesData);
        HttpHeaders.setContentLength(res, bytesData.readableBytes());
        return res;
    }
}
