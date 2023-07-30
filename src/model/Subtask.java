package model;

import java.util.Objects;

public class Subtask extends Task {
	int epicId;
	public Subtask(int epicId, String name, String description, Status status) {
		super(name, description, status);
		this.epicId = epicId;
	}

	public int getEpicId() {
		return epicId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Subtask subtask = (Subtask) o;
		return Objects.equals(name, subtask.name) && Objects.equals(description, subtask.description)
				&& Objects.equals(epicId, subtask.epicId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, description, epicId);
	}

	@Override
	public String toString() {
		return "Subtask{" +
				"epicId=" + epicId +
				", id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", status=" + status +
				'}';
	}
}
