package com.brokepal.nighty.login.sys.storage_object;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by chenchao on 17/3/31.
 */
public class SendEmailInfo implements Serializable {
    private static final long serialVersionUID = -201703312339000L;
    private Date sendEmailTime;
    private String validateCode;

    public SendEmailInfo(Date sendEmailTime, String validateCode) {
        this.sendEmailTime = sendEmailTime;
        this.validateCode = validateCode;
    }

    public Date getSendEmailTime() {
        return sendEmailTime;
    }

    public String getValidateCode() {
        return validateCode;
    }
}
