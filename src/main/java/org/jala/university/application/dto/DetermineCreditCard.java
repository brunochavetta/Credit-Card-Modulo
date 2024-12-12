package org.jala.university.application.dto;

public class DetermineCreditCard {
    private CreditCardTypeDTO type; 
    private Exception error; 

    public DetermineCreditCard () {
        this.type = new CreditCardTypeDTO(); 
    }

    public CreditCardTypeDTO getType() {
        return type;
    }

    public void setType(CreditCardTypeDTO type) {
        this.type = type;
    }

    public Exception getError() {
        return error;
    }

    public void setError(Exception error) {
        this.error = error;
    } 
}
