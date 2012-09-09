package com.jeffgabriel.TaskManager.Validators;

import android.content.Context;

import com.jeffgabriel.TaskManager.Interfaces.IValidate;

public abstract class ValidatorBase<T> implements IValidate<T> {
	protected static final String EmptyMessage = "";
	
	Context _context;
	public void set_Context(Context context){
		_context = context;
	}
	
	public Context get_Context(){
		return _context;
	}
	
	public abstract String Validate(T toValidate);

	protected boolean _valid = true;
	public boolean IsValid() {
		return _valid;
	}

}
