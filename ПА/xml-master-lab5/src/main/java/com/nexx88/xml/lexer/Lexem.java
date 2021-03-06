package com.nexx88.xml.lexer;

public class Lexem {
    private final Token token;
    private final String value;

    public Lexem(Token token, String value) {
        this.token = token;
        this.value = value;
    }

    public Token getToken() {
        return token;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Lexem{" +
                "token=" + token +
                ", value='" + value + '\'' +
                '}';
    }
}
