package manager;

import model.Task;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.TreeSet;

public class PrioritizedManager {
	private final Set<Task> prioritizedTasks;

	public PrioritizedManager() {
		prioritizedTasks = new TreeSet<>((t1, t2) -> {
			if(t1.getStartTime() == null && t2.getStartTime() != null)
				return 1;
			if(t1.getStartTime() != null && t2.getStartTime() == null)
				return -1;
			if(t1.getStartTime() == null && t2.getStartTime() == null)
				return t1.getId() - t2.getId();

			if (t1.getStartTime().isAfter(t2.getStartTime()))
				return 1;
			if (t1.getStartTime().isEqual(t2.getStartTime()))
				return 0;
			else
				return -1;
		});
	}

	public void add(Task task) {
		prioritizedTasks.add(task);
	}

	public void delete(Task task) {
		prioritizedTasks.remove(task);
	}

	public void update(Task oldTask, Task newTask) {
		prioritizedTasks.remove(oldTask);
		prioritizedTasks.add(newTask);
	}

	public boolean isTimeOverlap(Task task) {
		if (task.getStartTime() == null)
			return false;
		LocalDateTime startTime = task.getStartTime();
		LocalDateTime endTime = task.getEndTime();
		return prioritizedTasks.stream()
				.anyMatch(t -> {
					return t.getStartTime().isBefore(endTime) && t.getEndTime().isAfter(endTime)
							|| t.getStartTime().isBefore(startTime) && t.getEndTime().isAfter(startTime)
							|| t.getStartTime().isAfter(startTime) && t.getEndTime().isBefore(endTime);
				});
	}

	public Set<Task> getPrioritizedTasks() {
		return prioritizedTasks;
	}
}
