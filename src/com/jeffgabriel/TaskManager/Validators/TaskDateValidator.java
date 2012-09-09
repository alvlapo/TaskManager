package com.jeffgabriel.TaskManager.Validators;

import java.util.Calendar;

import com.jeffgabriel.TaskManager.R;
import com.jeffgabriel.TaskManager.Task;

public class TaskDateValidator extends ValidatorBase<Task> {

	public String Validate(Task toValidate) {
		Calendar cal = Calendar.getInstance();
		if (toValidate.get_dueDate().before(cal.getTime())) {
			_valid = false;
			return this.get_Context().getResources().getString(
					R.string.taskDateInPastValidationMessage);
		}
		return EmptyMessage;
	}
}
