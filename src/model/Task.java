package model;

import java.util.Objects;

public class Task {
	final int id;
	final String name;
	final String description;
	final Status status;

	public Task(int id, String name, String description, Status status) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public Status getStatus() {
		return status;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Task task = (Task) o;
		return Objects.equals(name, task.name) && Objects.equals(description, task.description);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, description);
	}

	@Override
	public String toString() {
		return "Task{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", status=" + status +
				'}';
	}
}
