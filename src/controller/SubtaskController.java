package controller;

import java.util.List;

import model.Subtask;

public interface SubtaskController {
	List<Subtask> getAllByEpicId(int epicId);

	Subtask getSubtaskById(int id);

	Subtask add(Subtask subtask);

	Subtask update(Subtask updatedSubtask);

	Subtask deleteSubtask(int id);
}
