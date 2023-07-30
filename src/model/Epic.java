package model;

import java.util.ArrayList;
import java.util.Arrays;

public class Epic extends Task {
	public Epic(String name, String description) {
		super(name, description, Status.NEW);
	}

	@Override
	public String toString() {
		return "Epic{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", status=" + status +
				'}';
	}
}
