package com.jeffgabriel.RoundTuit.Validators;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.jeffgabriel.RoundTuit.Task;

public class TaskValidator extends ValidatorBase<Task> {
	List<ValidatorBase<Task>> _validators = new ArrayList<ValidatorBase<Task>>();
	public TaskValidator(Context context){
		this.set_Context(context);
		_validators.add(new TaskDateValidator());
		_validators.add(new TaskNameValidator());
	}
	
	public String Validate(Task toValidate) {
		String message = "";
		for(int x = 0 ; x < _validators.size() ; x++){
			ValidatorBase<Task> validator = _validators.get(x);
			validator.set_Context(this.get_Context());
			message += validator.Validate(toValidate) + "\r\n";
			_valid = _valid & validator.IsValid();
		}
		return message;
	}
}