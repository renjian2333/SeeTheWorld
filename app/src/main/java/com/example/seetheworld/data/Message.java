package com.example.seetheworld.data;

public class Message<T> {
    private boolean state;
    private String msg;
    private T result;

    public Message(boolean state, String msg, T obj) {
        this.state = state;
        this.msg = msg;
        this.result = obj;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}