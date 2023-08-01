package model;

import java.util.ArrayList;
import java.util.Arrays;

public class Epic extends Task {
	private final ArrayList<Integer> subtasksId;

	public Epic(String name, String description) {
		super(name, description, Status.NEW);
		subtasksId = new ArrayList<>();
	}

	public void addSubtaskId(Integer id) {
		subtasksId.add(id);
	}

	public void deleteSubtaskId(Integer id) {
		subtasksId.remove(id);
	}

	@Override
	public String toString() {
		return "Epic{" +
				"subtasksId" + Arrays.toString(subtasksId.toArray()) +
				", id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", status=" + status +
				'}';
	}
}
