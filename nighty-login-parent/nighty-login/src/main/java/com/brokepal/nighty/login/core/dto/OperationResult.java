package com.brokepal.nighty.login.core.dto;

/**
 * Created by chenchao on 17/3/28.
 */

import java.io.Serializable;

/**
 * 表示操作的结果
 */
public class OperationResult implements Serializable {
    private static final long serialVersionUID = -201703302145000L;

    private static final String SUCCESS="success";
    private static final String FAILURE="failure";

    /** 返回success或failure操作标识 */
    private String type;

    /** 国际化处理的返回JSON消息正文，一般用于提供failure错误消息；success时如果返回的数据是字符串，可也存在message中 */
    private String message;

    /** 补充的业务数据 */
    private Object data;

    private OperationResult() {}

    private OperationResult(String type, String message) {
        this.type = type;
        this.message = message;
    }

    private OperationResult(String type, String message, Object data) {
        this.type = type;
        this.message = message;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static OperationResult buildSuccessResult() {
        OperationResult result = new OperationResult();
        result.type = SUCCESS;
        return result;
    }

    public static OperationResult buildSuccessResult(String message) {
        return new OperationResult(SUCCESS, message);// .setCode(SUCCESS);
    }

    public static OperationResult buildSuccessResult(Object data) {
        return new OperationResult(SUCCESS, "success", data);// .setCode(SUCCESS);
    }

    public static OperationResult buildSuccessResult(String message, Object data) {
        return new OperationResult(SUCCESS, message, data);// .setCode(SUCCESS);
    }

    public static OperationResult buildFailureResult(String message) {
        return new OperationResult(FAILURE, message);// .setCode(FAILURE);
    }

    public static OperationResult buildFailureResult(String message, Object data) {
        return new OperationResult(FAILURE, message, data);// .setCode(FAILURE);
    }

}

