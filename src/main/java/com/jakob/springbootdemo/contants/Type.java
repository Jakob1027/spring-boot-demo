package com.jakob.springbootdemo.contants;


import com.fasterxml.jackson.annotation.JsonCreator;

public enum Type {
    INTEGER, LONG;

    @Override
    public String toString() {
        return name();
    }

    @JsonCreator
    public static Type forName(String name) {
        for (Type t : values()) {
            if (t.name().equals(name)) { //change accordingly
                return t;
            }
        }

        return null;
    }
}
