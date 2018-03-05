package com.example.rejectcall.model;

/**
 * Created by Prakash on 4/15/2017.
 */

public class CallSms {

    public String api_id;

    public String getId() {
        return api_id;
    }

    public void setId(String api_id) {
        this.api_id = api_id;
    }

    public String api_msg;
    public String api_ts;
    public String api_type;
    public String api_issync;

    public String getApi_ringcount() {
        return api_ringcount;
    }

    public void setApi_ringcount(String api_ringcount) {
        this.api_ringcount = api_ringcount;
    }

    public String api_ringcount;

    public String getApi_token() {
        return api_token;
    }

    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }

    public String api_token;

    public CallSms()
    {

    }

    public String getMessage() {
        return api_msg;
    }

    public void setMessage(String api_msg) {
        this.api_msg = api_msg;
    }

    public String getTime() {
        return api_ts;
    }

    public void setTime(String api_ts) {
        this.api_ts = api_ts;
    }

    public String getType() {
        return api_type;
    }

    public void setType(String api_type) {
        this.api_type = api_type;
    }

    public String getIsSync() {
        return api_issync;
    }

    public void setIsSync(String api_issync) {
        this.api_issync = api_issync;
    }

    public String getSenderPhone() {
        return api_usrmobno;
    }

    public void setSenderPhone(String api_usrmobno) {
        this.api_usrmobno = api_usrmobno;
    }

    public String getOwnerPhone() {
        return api_mobno;
    }

    public void setOwnerPhone(String api_mobno) {
        this.api_mobno = api_mobno;
    }

    public String api_usrmobno;
    public String api_mobno;

    public CallSms(String api_usrmobno,String api_mobno,String api_msg,String api_ts,String api_type,String api_issync,String api_token,String api_ringcount)
    {
        this.api_usrmobno=api_usrmobno;
        this.api_mobno=api_mobno;
        this.api_msg=api_msg;
        this.api_ts=api_ts;
        this.api_type=api_type;
        this.api_issync=api_issync;
        this.api_token=api_token;
        this.api_ringcount=api_ringcount;

    }
}
