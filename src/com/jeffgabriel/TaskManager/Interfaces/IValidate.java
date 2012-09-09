package com.jeffgabriel.TaskManager.Interfaces;

public interface IValidate<T> {
	String Validate(T toValidate);
	boolean IsValid();
}
