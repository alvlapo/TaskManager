package com.jeffgabriel.TaskManager.Validators;

import com.jeffgabriel.TaskManager.R;
import com.jeffgabriel.TaskManager.Task;

public class TaskNameValidator extends ValidatorBase<Task> {
	
	public String Validate(Task toValidate) {
		if(toValidate.get_name() == null || toValidate.get_name().length() <= 0){
			_valid = false;
			return this.get_Context().getResources().getString(R.string.taskNameValidationMessage);
		}
		return EmptyMessage;
	}
}
