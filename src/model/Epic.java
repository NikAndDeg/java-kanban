package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Epic extends Task {
	private final ArrayList<Integer> subtasksId;

	public Epic(String name, String description) {
		super(name, description, Status.NEW);
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
