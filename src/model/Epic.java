package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Epic extends Task {
	public static final LocalDateTime DEFAULT_TIME = LocalDateTime.MIN;
	private final ArrayList<Integer> subtasksId;

	public Epic(String name, String description) {
		super(name, description, Status.NEW, Duration.ZERO, DEFAULT_TIME);
		subtasksId = new ArrayList<>();
	}

	public List<Integer> getSubtasksId() {
		return subtasksId;
	}

	public void addSubtaskId(Integer id) {
		subtasksId.add(id);
	}

	public void deleteSubtaskId(Integer id) {
		subtasksId.remove(id);
	}

	@Override
	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	@Override
	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	@Override
	public void update(Task updated) {
		this.name = updated.name;
		this.description = updated.description;
	}

	@Override
	public String toString() {
		return "Epic{" +
				"subtasksId" + Arrays.toString(subtasksId.toArray()) +
				", id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", status=" + status + '\'' +
				", duration=" + duration + '\'' +
				", startTime=" + startTime + '\'' +
				", endTime=" + endTime +
				'}';
	}
}
