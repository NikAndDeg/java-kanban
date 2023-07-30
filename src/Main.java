import controller.EpicController;
import controller.TaskController;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

public class Main {

    public static void main(String[] args) {
        EpicController ec = new EpicController();
        Epic epic = null;

        //тест на добавление эпика
        System.out.println("тест на добавление эпика");
        epic = ec.add(new Epic("first epic", "description"));
        System.out.println("Эпик из метода: " + epic);
        System.out.println("Все эпики: " + ec.getAll());
        System.out.println();

        //тест на добавление уже существующего эпика
        System.out.println("тест на добавление уже существующего эпика");
        epic = ec.add(new Epic("first epic", "description"));
        System.out.println("Эпик из метода: " + epic);
        System.out.println("Все эпики: " + ec.getAll());
        System.out.println();

        //тест на обновление эпика
        System.out.println("тест на обновление эпика");
        epic = new Epic("updated first epic", "");
        epic.setId(1);
        epic = ec.update(epic);
        System.out.println("Эпик из метода: " + epic);
        System.out.println("Все эпики: " + ec.getAll());
        System.out.println();

        //тест на обновление несуществующего эпика
        System.out.println("тест на обновление несуществующего эпика");
        epic = new Epic("updated non-existed epic", "");
        epic.setId(123456789);
        epic = ec.update(epic);
        System.out.println("Эпик из метода: " + epic);
        System.out.println("Все эпики: " + ec.getAll());
        System.out.println();

        //тест на удаление несуществующего эпика
        System.out.println("тест на удаление несуществующего эпика");
        epic = ec.delete(123456789);
        System.out.println("Эпик из метода: " + epic);
        System.out.println("Все эпики: " + ec.getAll());
        System.out.println();

        //тест на удаление эпика
        System.out.println("тест на удаление эпика");
        epic = ec.delete(1);
        System.out.println("Эпик из метода: " + epic);
        System.out.println("Все эпики: " + ec.getAll());
        System.out.println();

        ec.add(new Epic("second epic", "description"));

        //тест на добавление подзадачи
        System.out.println("тест на добавление подзадачи");
        Subtask subtask = ec.add(new Subtask(2, "first subtask", "description", Status.NEW));
        System.out.println("Подзадача из метода: " + subtask);
        System.out.println("Эпик: " + ec.getById(2));
        System.out.println();

        //тест на смену статуса эпика
        System.out.println("тест на смену статуса эпика");
        subtask = new Subtask(2, "updated subtask", "description", Status.DONE);
        subtask.setId(3);
        ec.update(subtask);
        System.out.println("Подзадача из метода: " + subtask);
        System.out.println("Эпик: " + ec.getById(2));
        System.out.println();

        subtask = ec.add(new Subtask(2, "third subtask", "description", Status.IN_PROGRESS));
        System.out.println("Подзадача из метода: " + subtask);
        System.out.println("Эпик: " + ec.getById(2));
        System.out.println();

        ec.add(new Subtask(2, "fourth subtask", "description", Status.NEW));
        ec.deleteSubtask(3);
        ec.deleteSubtask(4);
        System.out.println("Эпик: " + ec.getById(2));
        System.out.println();

        //тест на удаление ВСЕГО!!!!!111
        System.out.println("тест на удаление ВСЕГО!!!!!111");
        ec.deleteAll();
        System.out.println("Эпики: " + ec.getAll());
        System.out.println();

        TaskController tc = new TaskController();
        Task task = null;

        //тест на добавление задачи
        System.out.println("тест на добавление задачи");
        task = tc.add(new Task("first task", "description", Status.NEW));
        tc.add(new Task("second task", "description", Status.IN_PROGRESS));
        System.out.println("Задача из метода: " + task);
        System.out.println("Все задачи: " + tc.getAll());
        System.out.println();

        //тест на добавление уже существующей задачи
        System.out.println("тест на добавление уже существующей задачи");
        task = tc.add(new Task("first task", "description", Status.NEW));
        System.out.println("Задача из метода: " + task);
        System.out.println("Все задачи: " + tc.getAll());
        System.out.println();

        //тест на обновление задачи
        System.out.println("тест на обновление задачи");
        task = new Task("updated task", "description", Status.DONE);
        task.setId(1);
        task = tc.update(task);
        System.out.println("Задача из метода: " + task);
        System.out.println("Все задачи: " + tc.getAll());
        System.out.println();

        //тест на обновление несуществующей задачи
        System.out.println("тест на обновление несуществующей задачи");
        task = new Task("updated task", "description", Status.DONE);
        task.setId(123456789);
        task = tc.update(task);
        System.out.println("Задача из метода: " + task);
        System.out.println("Все задачи: " + tc.getAll());
        System.out.println();

        //тест на удаление несуществующей задачи
        System.out.println("тест на удаление несуществующей задачи");
        task = tc.delete(123456789);
        System.out.println("Задача из метода: " + task);
        System.out.println("Все задачи: " + tc.getAll());
        System.out.println();

        //тест на удаление задачи
        System.out.println("тест на удаление задачи");
        task = tc.delete(2);
        System.out.println("Задача из метода: " + task);
        System.out.println("Все задачи: " + tc.getAll());
        System.out.println();

        //тест на удаление ВСЕГО!!!!!111
        System.out.println("тест на удаление ВСЕГО!!!!!111");
        tc.deleteAll();
        System.out.println("Задачи: " + tc.getAll());
        System.out.println();
    }
}
