package com.example.TP_TDD_Eval_Antoine.Exception;

public class InvalidIsbnEmptyException extends Exception {
    public InvalidIsbnEmptyException(String message) {
        super(message);
    }
}