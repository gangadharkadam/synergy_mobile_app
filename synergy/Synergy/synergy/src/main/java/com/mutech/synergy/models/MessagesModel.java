package com.mutech.synergy.models;

import java.util.ArrayList;


public class MessagesModel {

    private ArrayList<MessagesModel> message;
    private String status;

    public ArrayList<MessagesModel> getMessage() {
        return message;
    }

    public void setMessage(ArrayList<MessagesModel> message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public class MessagesListModel {

        private String Msg;
        private String Date;

        public String getMsg() {
            return Msg;
        }

        public void setMsg(String msg) {
            Msg = msg;
        }

        public String getDate() {
            return Date;
        }

        public void setDate(String date) {
            Date = date;
        }
    }
}
