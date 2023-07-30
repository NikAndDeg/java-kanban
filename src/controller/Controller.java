package controller;

import java.util.List;

public interface Controller<T> {
	List<T> getAll();

	void deleteAll();

	T getById(int id);

	T add(T t);

	T update(T t);

	T delete(int id);
}
