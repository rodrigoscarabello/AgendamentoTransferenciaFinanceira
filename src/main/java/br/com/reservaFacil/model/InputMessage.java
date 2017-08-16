package br.com.reservaFacil.model;

public class InputMessage {

    public String inputName;
    public String message;

    public InputMessage(String inputName, String message) {
        this.inputName = inputName;
        this.message = message;
    }

    public String getInputName() {
        return inputName;
    }

    public void setInputName(String inputName) {
        this.inputName = inputName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
