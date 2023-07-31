import manager.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task task = null;
        Epic epic = null;
        Subtask subtask = null;

        //тесты эпиков и подзадач
        //тест на добавление эпика
        System.out.println("\nтесты эпиков и подзадач");
        System.out.println("тест на добавление эпика");
        epic = taskManager.add(new Epic("first epic", "description"));
        System.out.println("Эпик из метода: " + epic);
        System.out.println("Все эпики: " + taskManager.getEpics());
        System.out.println();

        //тест на добавление уже существующего эпика
        System.out.println("тест на добавление уже существующего эпика");
        epic = taskManager.add(new Epic("first epic", "description"));
        System.out.println("Эпик из метода: " + epic);
        System.out.println("Все эпики: " + taskManager.getEpics());
        System.out.println();

        //тест на обновление эпика
        System.out.println("тест на обновление эпика");
        epic = new Epic("updated first epic", "");
        epic.setId(1);
        epic = taskManager.update(epic);
        System.out.println("Эпик из метода: " + epic);
        System.out.println("Все эпики: " + taskManager.getEpics());
        System.out.println();

        //тест на обновление несуществующего эпика
        System.out.println("тест на обновление несуществующего эпика");
        epic = new Epic("updated non-existed epic", "");
        epic.setId(123456789);
        epic = taskManager.update(epic);
        System.out.println("Эпик из метода: " + epic);
        System.out.println("Все эпики: " + taskManager.getEpics());
        System.out.println();

        //тест на удаление несуществующего эпика
        System.out.println("тест на удаление несуществующего эпика");
        epic = taskManager.deleteEpic(123456789);
        System.out.println("Эпик из метода: " + epic);
        System.out.println("Все эпики: " + taskManager.getEpics());
        System.out.println();

        //тест на удаление эпика
        System.out.println("тест на удаление эпика");
        epic = taskManager.deleteEpic(1);
        System.out.println("Эпик из метода: " + epic);
        System.out.println("Все эпики: " + taskManager.getEpics());
        System.out.println();

        taskManager.add(new Epic("second epic", "description"));

        //тест на добавление подзадачи
        System.out.println("тест на добавление подзадачи");
        subtask = taskManager.add(new Subtask(2, "first subtask", "description", Status.NEW));
        System.out.println("Подзадача из метода: " + subtask);
        System.out.println("Эпик: " + taskManager.getEpic(2));
        System.out.println();

        //тест на смену статуса эпика
        System.out.println("тест на смену статуса эпика");
        System.out.println("обновляем статус подзадачи с NEW на DONE");
        subtask = new Subtask(2, "updated subtask", "description", Status.DONE);
        subtask.setId(3);
        taskManager.update(subtask);
        System.out.println("Подзадача из метода: " + subtask);
        System.out.println("Эпик: " + taskManager.getEpic(2));
        System.out.println();

        System.out.println("добавляем подзадачу со статусом IN_PROGRESS");
        subtask = taskManager.add(new Subtask(2, "third subtask", "description", Status.IN_PROGRESS));
        System.out.println("Подзадача из метода: " + subtask);
        System.out.println("Эпик: " + taskManager.getEpic(2));
        System.out.println();

        System.out.println("добавляем подзадачу со статусом NEW и удаляем остальные подзадачи");
        taskManager.add(new Subtask(2, "fourth subtask", "description", Status.NEW));
        taskManager.deleteSubtask(3);
        taskManager.deleteSubtask(4);
        System.out.println("Эпик: " + taskManager.getEpic(2));
        System.out.println();

        //тест на удаление всех эпиков
        System.out.println("тест на удаление всех эпиков");
        taskManager.deleteEpics();
        System.out.println("Эпики: " + taskManager.getEpics());
        System.out.println();

        taskManager = new TaskManager();
        //тесты задач
        //тест на добавление задачи
        System.out.println("\nтесты задач");
        System.out.println("тест на добавление задачи");
        task = taskManager.add(new Task("first task", "description", Status.NEW));
        taskManager.add(new Task("second task", "description", Status.IN_PROGRESS));
        System.out.println("Задача из метода: " + task);
        System.out.println("Все задачи: " + taskManager.getTasks());
        System.out.println();

        //тест на добавление уже существующей задачи
        System.out.println("тест на добавление уже существующей задачи");
        task = taskManager.add(new Task("first task", "description", Status.NEW));
        System.out.println("Задача из метода: " + task);
        System.out.println("Все задачи: " + taskManager.getTasks());
        System.out.println();

        //тест на обновление задачи
        System.out.println("тест на обновление задачи");
        task = new Task("updated task", "description", Status.DONE);
        task.setId(1);
        task = taskManager.update(task);
        System.out.println("Задача из метода: " + task);
        System.out.println("Все задачи: " + taskManager.getTasks());
        System.out.println();

        //тест на обновление несуществующей задачи
        System.out.println("тест на обновление несуществующей задачи");
        task = new Task("updated task", "description", Status.DONE);
        task.setId(123456789);
        task = taskManager.update(task);
        System.out.println("Задача из метода: " + task);
        System.out.println("Все задачи: " + taskManager.getTasks());
        System.out.println();

        //тест на удаление несуществующей задачи
        System.out.println("тест на удаление несуществующей задачи");
        task = taskManager.deleteTask(123456789);
        System.out.println("Задача из метода: " + task);
        System.out.println("Все задачи: " + taskManager.getTasks());
        System.out.println();

        //тест на удаление задачи
        System.out.println("тест на удаление задачи");
        task = taskManager.deleteTask(2);
        System.out.println("Задача из метода: " + task);
        System.out.println("Все задачи: " + taskManager.getTasks());
        System.out.println();

        //тест на удаление всех задач
        System.out.println("тест на удаление всех задач");
        taskManager.deleteTasks();
        System.out.println("Задачи: " + taskManager.getTasks());
        System.out.println();
    }
}
