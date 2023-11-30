package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
	protected int id;
	protected String name;
	protected String description;
	protected Status status;
	protected Duration duration;
	protected LocalDateTime startTime;
	protected LocalDateTime endTime;

	public Task(String name, String description, Status status, Duration duration,
				LocalDateTime startTime) {
		id = 0;
		this.name = name;
		this.description = description;
		this.status = status;
		this.duration = duration;
		this.startTime = startTime;
		this.endTime = startTime.plus(duration);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
		this.endTime = startTime.plus(duration);
	}

	public Duration getDuration() {
		return duration;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
		this.endTime = startTime.plus(duration);
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void update(Task updated) {
		this.name = updated.name;
		this.description = updated.description;
		this.status = updated.status;
		this.duration = updated.duration;
		this.startTime = updated.startTime;
		this.endTime = updated.endTime;
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
				", status=" + status + '\'' +
				", duration=" + duration + '\'' +
				", startTime=" + startTime + '\'' +
				", endTime=" + endTime +
				'}';
	}
}
