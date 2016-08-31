package com.iotracks.ws.manager.handler;

import com.iotracks.tmg.manager.TMGMessageManager;
import com.iotracks.utils.IOMessageUtils;
import com.iotracks.utils.IOFabricResponseUtils;
import com.iotracks.utils.elements.IOMessage;
import com.iotracks.utils.elements.LocalAPIURLType;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.*;
import io.netty.util.internal.StringUtil;

import javax.json.*;
import java.io.StringReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * HttpRequest handler for all REST requests to ioFabric.
 */
public class HttpRequestHandler implements Callable {

    private static final Logger log = Logger.getLogger(HttpRequestHandler.class.getName());

    private final String ID_PARAM_NAME = "id";
    private final String TIMEFRAME_START_PARAM_NAME = "timeframestart";
    private final String TIMEFRAME_END_PARAM_NAME = "timeframeend";
    private final String PUBLISHERS_PARAM_NAME = "publishers";

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
            return sendErrorResponse(Collections.singleton(" # Error: Incorrect HTTP method type."));
        }

        if(!(headers.get(HttpHeaders.Names.CONTENT_TYPE).equals("application/json"))){
            return sendErrorResponse(Collections.singleton(" # Error: Incorrect HTTP headers."));
        }

        ByteBuf msgBytes = req.content();
        String requestBody = msgBytes.toString(io.netty.util.CharsetUtil.US_ASCII);
        JsonReader reader = Json.createReader(new StringReader(requestBody));
        JsonObject jsonObject = reader.readObject();

        switch (urlType) {
            case GET_CONFIG_REST_LOCAL_API:
                return handleGetConfigRequest(jsonObject);
            case GET_NEXT_MSG_REST_LOCAL_API:
                return handleNextMessageRequest(jsonObject);
            case POST_MSG_REST_LOCAL_API:
                return handleNewMessageRequest(jsonObject);
            case GET_MSGS_QUERY_REST_LOCAL_API:
                return handleMessagesQueryRequest(jsonObject);
        }
        return sendErrorResponse(Collections.singleton("# Error: Unhandled request call."));
    }

    private void checkField(JsonObject jsonObject, String fieldName, Set<String > errors){
        if(!jsonObject.containsKey(fieldName)){
            errors.add(" # Error: Missing input field '" + fieldName +  "'.");
        }
    }

    private void parseLongField(JsonObject jsonObject, String fieldName, Set<String > errors){
        try{
            if(jsonObject.containsKey(fieldName)) {
                Long.parseLong(jsonObject.getJsonNumber(fieldName).toString());
            }
        } catch(Exception e){
            errors.add(" # Error: Invalid value of '" + fieldName + "'.");
        }
    }

    private void parseIntField(JsonObject jsonObject, String fieldName, Set<String > errors){
        parseFieldWithPattern(jsonObject, fieldName, errors, "[0-9]+");
    }

    private void parseFieldWithPattern(JsonObject jsonObject, String fieldName, Set<String > errors, String pattern){
        if(jsonObject.containsKey(fieldName)){
            String number = jsonObject.getJsonNumber(fieldName).toString();
            if(!(number.matches(pattern))){
                errors.add(" # Error: Invalid  value for field '" + fieldName + "'.");
            }
        }
    }

    private void parseStringField(JsonObject jsonObject, String fieldName, Set<String > errors){
        if(jsonObject.containsKey(fieldName) && StringUtil.isNullOrEmpty(jsonObject.getString(fieldName))) {
            errors.add(" # Error: Missing input field value for '" + fieldName + "'.");
        }
    }

    private void validateMessage(JsonObject jsonObject, Set<String> errors){
        checkField(jsonObject, IOMessage.PUBLISHER_FIELD_NAME, errors);
        checkField(jsonObject, IOMessage.VERSION_FIELD_NAME, errors);
        checkField(jsonObject, IOMessage.INFO_TYPE_FIELD_NAME, errors);
        checkField(jsonObject, IOMessage.INFO_FORMAT_FIELD_NAME, errors);
        checkField(jsonObject, IOMessage.CONTENT_DATA_FIELD_NAME, errors);

        parseStringField(jsonObject, IOMessage.PUBLISHER_FIELD_NAME, errors);
        parseStringField(jsonObject, IOMessage.INFO_TYPE_FIELD_NAME, errors);
        parseStringField(jsonObject, IOMessage.INFO_FORMAT_FIELD_NAME, errors);

        parseIntField(jsonObject, IOMessage.VERSION_FIELD_NAME, errors);
        parseIntField(jsonObject, IOMessage.SEQUENCE_NUMBER_FIELD_NAME, errors);
        parseIntField(jsonObject, IOMessage.SEQUENCE_TOTAL_FIELD_NAME, errors);
        parseIntField(jsonObject, IOMessage.PRIORITY_FIELD_NAME, errors);
        parseIntField(jsonObject, IOMessage.CHAIN_POSITION_FIELD_NAME, errors);

        parseFieldWithPattern(jsonObject, IOMessage.DIFFICULTY_TARGET_FIELD_NAME, errors, "[0-9]*.?[0-9]*");
    }

    private void validateMessageID(JsonObject jsonObject, Set<String> errors){
        checkField(jsonObject, ID_PARAM_NAME, errors);
        if(jsonObject.containsKey(ID_PARAM_NAME) && StringUtil.isNullOrEmpty(jsonObject.getString(ID_PARAM_NAME))){
            errors.add(" # Error: Missing input field '" + ID_PARAM_NAME + "' value.");
            return;
        }
    }

    private void validateMessageQuery(JsonObject jsonObject, Set<String> errors){
        validateMessageID(jsonObject, errors);
        checkField(jsonObject, PUBLISHERS_PARAM_NAME, errors);
        checkField(jsonObject, TIMEFRAME_START_PARAM_NAME, errors);
        checkField(jsonObject, TIMEFRAME_END_PARAM_NAME, errors);

        parseLongField(jsonObject, TIMEFRAME_START_PARAM_NAME, errors);
        parseLongField(jsonObject, TIMEFRAME_END_PARAM_NAME, errors);
    }

    private FullHttpResponse sendErrorResponse(Set<String> errors){
        errors.forEach(error -> bytesData.writeBytes(error.getBytes()));
        return new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.BAD_REQUEST, bytesData);
    }

    private FullHttpResponse sendResponse(){
        FullHttpResponse res = new DefaultFullHttpResponse(HTTP_1_1, OK, bytesData);
        HttpHeaders.setContentLength(res, bytesData.readableBytes());
        return res;
    }

    private JsonObject buildMessagesResponse(List<IOMessage> messages, boolean isQueryRequest) {
        JsonArrayBuilder messagesBuilder = Json.createArrayBuilder();
        messages.forEach(message -> messagesBuilder.add(message.getJson()));
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder()
                .add(IOFabricResponseUtils.STATUS_FIELD_NAME, "okay")
                .add(IOFabricResponseUtils.COUNT_FIELD_NAME, messages.size())
                .add(IOFabricResponseUtils.MESSAGES_FIELD_NAME, messagesBuilder);
        if(isQueryRequest){
            jsonBuilder.add(IOFabricResponseUtils.TIMEFRAME_START_FIELD_NAME, System.currentTimeMillis())
                    .add(IOFabricResponseUtils.TIMEFRAME_END_FIELD_NAME, System.currentTimeMillis());
        }
        return jsonBuilder.build();
    }

    private FullHttpResponse handleGetConfigRequest(JsonObject jsonObject){
        Set<String> errors = new HashSet<>();
        validateMessageID(jsonObject, errors);
        if(!errors.isEmpty()) {
            return sendErrorResponse(errors);
        }
        bytesData.writeBytes(TMGMessageManager.getContainerConfig().toString().getBytes());
        //System.out.println("Sending config");
        return sendResponse();
    }

    private FullHttpResponse handleNextMessageRequest(JsonObject jsonObject){
        Set<String> errors = new HashSet<>();
        validateMessageID(jsonObject, errors);
        if(!errors.isEmpty()) {
            return sendErrorResponse(errors);
        }
        bytesData.writeBytes(buildMessagesResponse(Collections.singletonList(TMGMessageManager.getRandomMessage()), false).toString().getBytes());
        return sendResponse();
    }

    private FullHttpResponse handleNewMessageRequest(JsonObject jsonObject){
        Set<String> errors = new HashSet<>();
        validateMessage(jsonObject, errors);
        if(!errors.isEmpty()) {
            return sendErrorResponse(errors);
        }
        IOMessage newMessage = new IOMessage(jsonObject);
        newMessage.setId(IOMessageUtils.generateID());
        newMessage.setTimestamp(System.currentTimeMillis());
        TMGMessageManager.saveMessage(newMessage);

        JsonObject messageReceipt = Json.createObjectBuilder()
                .add(IOFabricResponseUtils.ID_FIELD_NAME, newMessage.getId())
                .add(IOFabricResponseUtils.TIMESTAMP_FIELD_NAME, newMessage.getTimestamp())
                .add(IOFabricResponseUtils.STATUS_FIELD_NAME, "okay").build();
        bytesData.writeBytes(messageReceipt.toString().getBytes());
        return sendResponse();
    }

    private Object handleMessagesQueryRequest(JsonObject jsonObject){
        Set<String> errors = new HashSet<>();
        validateMessageQuery(jsonObject, errors);
        if(!errors.isEmpty()) {
            return sendErrorResponse(errors);
        }
        bytesData.writeBytes(buildMessagesResponse(TMGMessageManager.getAllMessages(), true).toString().getBytes());
        return sendResponse();
    }
}
