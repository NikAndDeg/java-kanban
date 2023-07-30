package model;

import java.util.ArrayList;
import java.util.Arrays;

public class Epic extends Task {
	private final ArrayList<Integer> subtasksId;
	public Epic(int id, String name, String description) {
		super(id, name, description, Status.NEW);
		subtasksId = new ArrayList<>();
	}

	public ArrayList<Integer> getSubtasksId() {
		return subtasksId;
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
