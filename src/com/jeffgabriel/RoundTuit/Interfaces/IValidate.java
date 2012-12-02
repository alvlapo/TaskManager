package com.jeffgabriel.RoundTuit.Interfaces;

public interface IValidate<T> {
	String Validate(T toValidate);
	boolean IsValid();
}
