import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        TaskManager inMemoryTaskManager = Managers.getDefault();
        Task task = null;
        Epic epic = null;
        Subtask subtask = null;

        //тесты эпиков и подзадач
        //тест на добавление эпика
        System.out.println("\nтесты эпиков и подзадач");
        System.out.println("тест на добавление эпика");
        epic = inMemoryTaskManager.addEpic(new Epic("first epic", "description"));
        System.out.println("Эпик из метода: " + epic);
        System.out.println("Все эпики: " + inMemoryTaskManager.getEpics());
        System.out.println();

        //тест на добавление уже существующего эпика
        System.out.println("тест на добавление уже существующего эпика");
        epic = inMemoryTaskManager.addEpic(new Epic("first epic", "description"));
        System.out.println("Эпик из метода: " + epic);
        System.out.println("Все эпики: " + inMemoryTaskManager.getEpics());
        System.out.println();

        //тест на обновление эпика
        System.out.println("тест на обновление эпика");
        epic = new Epic("updated first epic", "");
        epic.setId(1);
        epic = inMemoryTaskManager.updateEpic(epic);
        System.out.println("Эпик из метода: " + epic);
        System.out.println("Все эпики: " + inMemoryTaskManager.getEpics());
        System.out.println();

        //тест на обновление несуществующего эпика
        System.out.println("тест на обновление несуществующего эпика");
        epic = new Epic("updated non-existed epic", "");
        epic.setId(123456789);
        epic = inMemoryTaskManager.updateEpic(epic);
        System.out.println("Эпик из метода: " + epic);
        System.out.println("Все эпики: " + inMemoryTaskManager.getEpics());
        System.out.println();

        //тест на удаление несуществующего эпика
        System.out.println("тест на удаление несуществующего эпика");
        epic = inMemoryTaskManager.deleteEpic(123456789);
        System.out.println("Эпик из метода: " + epic);
        System.out.println("Все эпики: " + inMemoryTaskManager.getEpics());
        System.out.println();

        //тест на удаление эпика
        System.out.println("тест на удаление эпика");
        epic = inMemoryTaskManager.deleteEpic(1);
        System.out.println("Эпик из метода: " + epic);
        System.out.println("Все эпики: " + inMemoryTaskManager.getEpics());
        System.out.println();

        inMemoryTaskManager.addEpic(new Epic("second epic", "description"));

        //тест на добавление подзадачи
        System.out.println("тест на добавление подзадачи");
        subtask = inMemoryTaskManager.addSubtask(new Subtask(2, "first subtask", "description", Status.NEW));
        System.out.println("Подзадача из метода: " + subtask);
        System.out.println("Эпик: " + inMemoryTaskManager.getEpic(2));
        System.out.println();

        //тест на смену статуса эпика
        System.out.println("тест на смену статуса эпика");
        System.out.println("обновляем статус подзадачи с NEW на DONE");
        subtask = new Subtask(2, "updated subtask", "description", Status.DONE);
        subtask.setId(3);
        inMemoryTaskManager.updateSubtask(subtask);
        System.out.println("Подзадача из метода: " + subtask);
        System.out.println("Эпик: " + inMemoryTaskManager.getEpic(2));
        System.out.println();

        System.out.println("добавляем подзадачу со статусом IN_PROGRESS");
        subtask = inMemoryTaskManager.addSubtask(new Subtask(2, "third subtask", "description", Status.IN_PROGRESS));
        System.out.println("Подзадача из метода: " + subtask);
        System.out.println("Эпик: " + inMemoryTaskManager.getEpic(2));
        System.out.println();

        System.out.println("добавляем подзадачу со статусом NEW и удаляем остальные подзадачи");
        inMemoryTaskManager.addSubtask(new Subtask(2, "fourth subtask", "description", Status.NEW));
        inMemoryTaskManager.deleteSubtask(3);
        inMemoryTaskManager.deleteSubtask(4);
        System.out.println("Эпик: " + inMemoryTaskManager.getEpic(2));
        System.out.println();

        //тест на удаление всех эпиков
        System.out.println("тест на удаление всех эпиков");
        inMemoryTaskManager.deleteEpics();
        System.out.println("Эпики: " + inMemoryTaskManager.getEpics());
        System.out.println();

        inMemoryTaskManager = new InMemoryTaskManager();
        //тесты задач
        //тест на добавление задачи
        System.out.println("\nтесты задач");
        System.out.println("тест на добавление задачи");
        task = inMemoryTaskManager.addTask(new Task("first task", "description", Status.NEW));
        inMemoryTaskManager.addTask(new Task("second task", "description", Status.IN_PROGRESS));
        System.out.println("Задача из метода: " + task);
        System.out.println("Все задачи: " + inMemoryTaskManager.getTasks());
        System.out.println();

        //тест на добавление уже существующей задачи
        System.out.println("тест на добавление уже существующей задачи");
        task = inMemoryTaskManager.addTask(new Task("first task", "description", Status.NEW));
        System.out.println("Задача из метода: " + task);
        System.out.println("Все задачи: " + inMemoryTaskManager.getTasks());
        System.out.println();

        //тест на обновление задачи
        System.out.println("тест на обновление задачи");
        task = new Task("updated task", "description", Status.DONE);
        task.setId(1);
        task = inMemoryTaskManager.updateTask(task);
        System.out.println("Задача из метода: " + task);
        System.out.println("Все задачи: " + inMemoryTaskManager.getTasks());
        System.out.println();

        //тест на обновление несуществующей задачи
        System.out.println("тест на обновление несуществующей задачи");
        task = new Task("updated task", "description", Status.DONE);
        task.setId(123456789);
        task = inMemoryTaskManager.updateTask(task);
        System.out.println("Задача из метода: " + task);
        System.out.println("Все задачи: " + inMemoryTaskManager.getTasks());
        System.out.println();

        //тест на удаление несуществующей задачи
        System.out.println("тест на удаление несуществующей задачи");
        task = inMemoryTaskManager.deleteTask(123456789);
        System.out.println("Задача из метода: " + task);
        System.out.println("Все задачи: " + inMemoryTaskManager.getTasks());
        System.out.println();

        //тест на удаление задачи
        System.out.println("тест на удаление задачи");
        task = inMemoryTaskManager.deleteTask(2);
        System.out.println("Задача из метода: " + task);
        System.out.println("Все задачи: " + inMemoryTaskManager.getTasks());
        System.out.println();

        //тест на удаление всех задач
        System.out.println("тест на удаление всех задач");
        inMemoryTaskManager.deleteTasks();
        System.out.println("Задачи: " + inMemoryTaskManager.getTasks());
        System.out.println();


        //тест истории
        System.out.println("тест истории");
        TaskManager taskManager = Managers.getDefault();

        taskManager.addTask(getNewTask());
        taskManager.addEpic(getNewEpic());
        taskManager.addSubtask(getNewSubtask(2));

        taskManager.getTask(1);
        taskManager.getEpic(2);
        taskManager.getSubtask(3);

        System.out.println(Arrays.toString(taskManager.getHistory().toArray()));

        taskManager.getTask(1);
        taskManager.getEpic(2);
        taskManager.getSubtask(3);
        taskManager.getTask(1);
        taskManager.getEpic(2);
        taskManager.getSubtask(3);
        taskManager.getTask(1);
        taskManager.getSubtask(3);
        taskManager.getSubtask(3);
        taskManager.getSubtask(3);

        System.out.println(Arrays.toString(taskManager.getHistory().toArray()));
    }

    public static Task getNewTask() {
        return new Task("Task", "description", Status.NEW);
    }

    public static Epic getNewEpic() {
        return new Epic("Task", "description");
    }

    public static Subtask getNewSubtask(int epicId) {
        return new Subtask(epicId, "Subtask", "description", Status.NEW);
    }
}
