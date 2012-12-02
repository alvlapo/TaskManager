package com.jeffgabriel.RoundTuit.Interfaces;

import java.util.List;

import com.jeffgabriel.RoundTuit.Task;

public interface ITaskProvider {

	public abstract void delete(com.jeffgabriel.RoundTuit.Task task);
	public abstract List<com.jeffgabriel.RoundTuit.Task> getAll();
	public abstract List<com.jeffgabriel.RoundTuit.Task> getSome(int pageIndex, int pageSize);
	public abstract void update(Task task);
	public abstract Task get(int id);

}