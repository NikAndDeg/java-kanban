package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
	private final int epicId;
	public Subtask(int epicId, String name, String description, Status status, Duration duration,
				   LocalDateTime startTime) {
		super(name, description, status, duration, startTime);
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
				", status=" + '\'' +
				", duration=" + duration + '\'' +
				", startTime=" + startTime + '\'' +
				", endTime=" + endTime +
				'}';
	}
}
