package test;

import manager.InMemoryTaskManager;
import manager.TaskManager;
import model.Epic;
import static model.Status.*;
import model.Subtask;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.LocalDateTime;

class EpicTimeTest {
	TaskManager manager;

	@BeforeEach
	public void createNewManager() {
		manager = new InMemoryTaskManager();
	}

	@Test
	public void epic_time_test_with_no_subtasks() {
		manager.addEpic(new Epic("epic", ""));
		Epic expected = new Epic("epic", "");

		assertEpic(expected, manager.getEpic(1));
	}

	@Test
	public void epic_time_test_with_one_subtask() {
		manager.addEpic(new Epic("epic", ""));
		manager.addSubtask(getSubtaskWithTime("subtask", 25, "2010-10-10T10:10:10"));

		Epic expected = getEpicWithTime(25,
				"2010-10-10T10:10:10",
				"2010-10-10T10:35:10");

		assertEpic(expected, manager.getEpic(1));
	}

	@Test
	public void epic_time_test_with_subtasks() {
		manager.addEpic(new Epic("epic", ""));
		manager.addSubtask(getSubtaskWithTime("first subtask", 10, "2010-10-10T10:10:10"));
		manager.addSubtask(getSubtaskWithTime("second subtask", 18, "2020-10-10T10:10:10"));

		Epic expected = getEpicWithTime(28,
				"2010-10-10T10:10:10",
				"2020-10-10T10:28:10");

		assertEpic(expected, manager.getEpic(1));
	}

	@Test
	public void epic_time_test_with_subtasks_when_one_subtask_is_deleted() {
		manager.addEpic(new Epic("epic", ""));
		manager.addSubtask(getSubtaskWithTime("first subtask", 10, "2010-10-10T10:10:10"));
		manager.addSubtask(getSubtaskWithTime("second subtask", 18, "2020-10-10T10:10:10"));

		manager.deleteSubtask(3);

		Epic expected = getEpicWithTime(10,
				"2010-10-10T10:10:10",
				"2010-10-10T10:20:10");

		assertEpic(expected, manager.getEpic(1));
	}

	@Test
	public void epic_time_test_with_subtasks_when_all_subtasks_are_deleted() {
		manager.addEpic(new Epic("epic", ""));
		manager.addSubtask(getSubtaskWithTime("first subtask", 10, "2010-10-10T10:10:10"));
		manager.addSubtask(getSubtaskWithTime("second subtask", 18, "2020-10-10T10:10:10"));

		manager.deleteSubtask(2);
		manager.deleteSubtask(3);

		Epic expected = new Epic("", "");

		assertEpic(expected, manager.getEpic(1));
	}

	@Test
	public void epic_time_test_with_updated_subtask() {
		manager.addEpic(new Epic("", ""));
		manager.addSubtask(getSubtaskWithTime("subtask", 25, "2010-10-10T10:10:10"));

		Subtask updatedSubtask = getSubtaskWithTime("subtask", 8, "2012-10-10T10:10:10");
		updatedSubtask.setId(2);
		manager.updateSubtask(updatedSubtask);

		Epic expected = getEpicWithTime(8, "2012-10-10T10:10:10", "2012-10-10T10:18:10");
		assertEpic(expected, manager.getEpic(1));
	}

	private void assertEpic(Epic expected, Epic actual) {
		assertTrue(expected.getStartTime().isEqual(actual.getStartTime()));
		assertEquals(expected.getDuration(), actual.getDuration());
		assertTrue(expected.getEndTime().isEqual(actual.getEndTime()));
	}

	private Epic getEpicWithTime(int durationInMinutes, String startTime, String endTime) {
		Epic epic = new Epic("epic", "");
		epic.setDuration(Duration.ofMinutes(durationInMinutes));
		epic.setStartTime(LocalDateTime.parse(startTime));
		epic.setEndTime(LocalDateTime.parse(endTime));
		return epic;
	}

	private Subtask getSubtaskWithTime(String name, int durationInMinutes, String startTime) {
		return new Subtask(1,
				name,
				"",
				NEW,
				Duration.ofMinutes(durationInMinutes),
				LocalDateTime.parse(startTime));
	}
}