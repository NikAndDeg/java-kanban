package test;

import exception.TimeOverlapException;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import model.Subtask;
import model.Task;
import static model.Status.*;

import java.time.Duration;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TimeOverlapTest {
	TaskManager manager;

	@BeforeEach
	public void createNewManager() {
		manager = new InMemoryTaskManager();
	}

	@Test
	public void time_overlap_test_when_second_task_start_between_start_and_end_first_task() {
		manager.addTask(createTask("first task", 20, "2000-10-10T10:10:10"));

		Exception exception = assertThrows(TimeOverlapException.class,
				() -> manager.addTask(createTask("second task", 20, "2000-10-10T10:11:10"))
		);
		String expectedMessage = "Time overlap with: " + createTask("second task",
				20, "2000-10-10T10:11:10");

		assertEquals(expectedMessage, exception.getMessage());
	}

	@Test
	public void time_overlap_test_when_second_task_end_between_start_and_end_first_task() {
		manager.addTask(createTask("first task", 20, "2000-10-10T10:10:10"));

		Exception exception = assertThrows(TimeOverlapException.class,
				() -> manager.addTask(createTask("second task", 20, "2000-10-10T10:00:10"))
		);
		String expectedMessage = "Time overlap with: " + createTask("second task",
				20, "2000-10-10T10:00:10");

		assertEquals(expectedMessage, exception.getMessage());
	}

	@Test
	public void time_overlap_test_when_second_task_start_and_end_between_start_and_end_first_task() {
		manager.addTask(createTask("first task", 20, "2000-10-10T10:10:10"));

		Exception exception = assertThrows(TimeOverlapException.class,
				() -> manager.addTask(createTask("second task", 5, "2000-10-10T10:15:10"))
		);
		String expectedMessage = "Time overlap with: " + createTask("second task",
				5, "2000-10-10T10:15:10");

		assertEquals(expectedMessage, exception.getMessage());
	}

	@Test
	public void time_overlap_test_when_second_task_start_before_first_start_and_second_end_after_first_end() {
		manager.addTask(createTask("first task", 20, "2000-10-10T10:10:10"));

		Exception exception = assertThrows(TimeOverlapException.class,
				() -> manager.addTask(createTask("second task", 60, "2000-10-10T10:05:10"))
		);
		String expectedMessage = "Time overlap with: " + createTask("second task",
				60, "2000-10-10T10:05:10");

		assertEquals(expectedMessage, exception.getMessage());
	}

	private Task createTask(String name, int durationInMinutes, String startTime) {
		return new Task(name,
				"",
				NEW,
				Duration.ofMinutes(durationInMinutes),
				LocalDateTime.parse(startTime)
		);
	}
}