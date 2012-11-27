package com.jeffgabriel.TaskManager;


public class TaskQuery {
	private String[] _whereParams;
	boolean showComplete = false;
	static final String StatusClause = " IsComplete = ? ";
	
	public TaskQuery(boolean showCompletedItems){
		showComplete = showCompletedItems;
		if (showComplete == false) {
			_whereParams = new String[] { "0" };
			_whereStatement = StatusClause;
		}
	}
	
	public String[] get_WhereParameters() {
		return _whereParams;
	}

	public void set_WhereParameters(String[] params) {
		String[] allParams = new String[params.length + 1];
		for(int index = 0 ; index < params.length ; index++)
			allParams[index] = params[index];
		allParams[params.length] = "0";
		_whereParams = allParams;
	}

	String get_whereStatement() {
		return _whereStatement != StatusClause && _whereStatement.length() > 0 ? _whereStatement + " AND " + StatusClause : _whereStatement;
	}

	void set_whereStatement(String whereStatement) {
		this._whereStatement = whereStatement;
	}

	private String _whereStatement = "";

}
