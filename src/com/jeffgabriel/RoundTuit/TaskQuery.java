package com.jeffgabriel.RoundTuit;

import java.util.ArrayList;

public class TaskQuery {
	private String[] _whereParams;
	boolean showComplete = false;
	static final String StatusClause = " IsComplete = ? ";

	public TaskQuery(boolean showCompletedItems) {
		showComplete = showCompletedItems;
		if (showComplete == false) {
			_whereParams = new String[] { "0" };
			_whereStatement = StatusClause;
		}
	}

	public String[] get_whereParameters() {
		return _whereParams;
	}

	public void set_whereParameters(ArrayList<String> params) {
		if (showComplete == false)
			params.add("0");
		_whereParams = params.toArray(new String[params.size()]);
	}

	String get_whereStatement() {
		if (_whereStatement == StatusClause)
			return StatusClause;
		String where = showComplete ? _whereStatement : (_whereStatement
				.length() > 0) ? _whereStatement + " AND " + StatusClause
				: StatusClause;
		return where;
	}

	void set_whereStatement(String whereStatement) {
		this._whereStatement = whereStatement;
	}

	private String _whereStatement = "";

}
