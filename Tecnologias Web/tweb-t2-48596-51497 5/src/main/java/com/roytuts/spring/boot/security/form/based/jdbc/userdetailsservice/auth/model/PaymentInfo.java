package com.roytuts.spring.boot.security.form.based.jdbc.userdetailsservice.auth.model;

import java.math.BigDecimal;

public class PaymentInfo {
    private String entity;
    private String reference;
    private BigDecimal value;

    public PaymentInfo() {
    }

    public PaymentInfo(String entity, String reference, BigDecimal value) {
        this.entity = entity;
        this.reference = reference;
        this.value = value;
    }

    public String getEntity() {
        return entity;
    }

    public String getReference() {
        return reference;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

}
