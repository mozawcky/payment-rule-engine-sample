package com.tvlk.payment.ruleengine.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Person {

    private String name;
    private int age;
    private GENDER gender;
    private boolean member;

    public boolean isAdult() {
        return age >= 18;
    }

    public enum GENDER {
        MALE,
        FEMALE
    }
}