package br.com.reservaFacil.model;

import java.util.ArrayList;
import java.util.List;

public class Message {

    public Boolean success;
    public String message;
    public List<InputMessage> inputMessages = new ArrayList<>();

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<InputMessage> getInputMessages() {
        return inputMessages;
    }

    public void setInputMessages(List<InputMessage> inputMessages) {
        this.inputMessages = inputMessages;
    }
}
