import manager.*;
import model.*;
import model.list.CustomLinkedList;

public class Main {

    public static void main(String[] args) {
        testCustomLinkedList();
        testHistory();
    }

    private static void testCustomLinkedList() {
        System.out.println();

        System.out.println("Тестирование CustomLinkedList:");
        System.out.println("Создание CustomLinkedList<Integer>.");
        CustomLinkedList<Integer> list = new CustomLinkedList<>();

        System.out.println();

        System.out.println("Заполнение значениями 10, 99, 500, 7");
        System.out.println("Получение значений по индексу:");
        list.linkLast(10);
        list.linkLast(99);
        list.linkLast(500);
        list.linkLast(7);
        System.out.print("10 по индексу 0: ");
        System.out.println(list.get(0).equals(10));
        System.out.print("99 по индексу 1: ");
        System.out.println(list.get(1).equals(99));
        System.out.print("500 по индексу 2: ");
        System.out.println(list.get(2).equals(500));
        System.out.print("7 по индексу 3: ");
        System.out.println(list.get(3).equals(7));
        System.out.print("null по индексу 999: ");
        System.out.println(list.get(999) == null);

        System.out.println();

        System.out.println("Удаление значений 10, 500 по индексам 0, 2:");
        System.out.print("Удалено значение 10: ");
        System.out.println(list.delete(0).equals(10));
        System.out.print("Удалено значение 500: ");
        System.out.println(list.delete(2).equals(500));
        System.out.println("Получение значений по индексу:");
        System.out.print("null по индексу 0: ");
        System.out.println(list.get(0) == null);
        System.out.print("99 по индексу 1: ");
        System.out.println(list.get(1).equals(99));
        System.out.print("null по индексу 2: ");
        System.out.println(list.get(2) == null);
        System.out.print("7 по индексу 3: ");
        System.out.println(list.get(3).equals(7));
        System.out.print("null по индексу 999: ");
        System.out.println(list.get(999) == null);

        System.out.println();

        System.out.println("Создание CustomLinkedList<Integer>");
        System.out.println("Заполнение значениями 10, 99, 500, 7");
        list = new CustomLinkedList<>();
        list.linkLast(10);
        list.linkLast(99);
        list.linkLast(500);
        list.linkLast(7);
        System.out.print("Получение списка [10, 99, 500, 7]: ");
        System.out.println(list.getAsList().toString().equals("[10, 99, 500, 7]"));
        System.out.println("Добавление значений-повторов 99 и 500.");
        list.linkLast(99);
        list.linkLast(500);
        System.out.print("Получение списка [10, 7, 99, 500]: ");
        System.out.println(list.getAsList().toString().equals("[10, 7, 99, 500]"));

        System.out.println();
    }

    private static void testHistory() {
        System.out.println("Тестирование истории: ");
        TaskManager tm = Managers.getDefault();

        System.out.println("Создание менеджера и заполнение его задачами 1, 2, 3, 4," +
                " эпиком 5 с подзадачами 6, 7 и пустым эпиком 8");
        /*
        Task{id=1, name='1', description='description', status=NEW}
        Task{id=2, name='2', description='description', status=NEW}
        Task{id=3, name='3', description='description', status=NEW}
        Task{id=4, name='4', description='description', status=NEW}
        Epic{subtasksId[6, 7], id=5, name='5', description='description', status=NEW}
        Subtask{epicId=5, id=6, name='6', description='description', status=NEW}
        Subtask{epicId=5, id=7, name='7', description='description', status=NEW}
        Epic{subtasksId[], id=8, name='8', description='description', status=NEW}
        */
        tm.addTask(getNewTask("1"));
        tm.addTask(getNewTask("2"));
        tm.addTask(getNewTask("3"));
        tm.addTask(getNewTask("4"));
        tm.addEpic(getNewEpic("5"));
        tm.addSubtask(getNewSubtask("6", 5));
        tm.addSubtask(getNewSubtask("7", 5));
        tm.addEpic(getNewEpic("8"));
        System.out.println();

        System.out.println("Вызов задач 1, 2, 4, 3");
        tm.getTask(1);
        tm.getTask(2);
        tm.getTask(4);
        tm.getTask(3);
        String s = "[" +
                "Task{id=1, name='1', description='description', status=NEW}," +
                " Task{id=2, name='2', description='description', status=NEW}," +
                " Task{id=4, name='4', description='description', status=NEW}," +
                " Task{id=3, name='3', description='description', status=NEW}" +
                "]";
        System.out.println("Вызов истории просмотра задач");
        System.out.println(tm.getHistory());
        System.out.print("Ожидаемые id в истории: 1, 2, 4, 3: ");
        System.out.println(tm.getHistory().toString().equals(s));
        System.out.println();

        System.out.println("Вызов задач 1, 1, 4");
        tm.getTask(1);
        tm.getTask(1);
        tm.getTask(4);
        s = "[" +
                "Task{id=2, name='2', description='description', status=NEW}," +
                " Task{id=3, name='3', description='description', status=NEW}," +
                " Task{id=1, name='1', description='description', status=NEW}," +
                " Task{id=4, name='4', description='description', status=NEW}" +
                "]";
        System.out.println("Вызов истории просмотра задач");
        System.out.println(tm.getHistory());
        System.out.print("Ожидаемые id в истории: 2, 3, 1, 4: ");
        System.out.println(tm.getHistory().toString().equals(s));
        System.out.println();

        System.out.println("Удаление задачи 3");
        tm.deleteTask(3);
        s = "[" +
                "Task{id=2, name='2', description='description', status=NEW}," +
                " Task{id=1, name='1', description='description', status=NEW}," +
                " Task{id=4, name='4', description='description', status=NEW}" +
                "]";
        System.out.println("Вызов истории просмотра задач");
        System.out.println(tm.getHistory());
        System.out.print("Ожидаемые id в истории: 2, 1, 4: ");
        System.out.println(tm.getHistory().toString().equals(s));
        System.out.println();

        System.out.println("Вызов эпика 8, подзадачи 7, эпика 5");
        tm.getEpic(8);
        tm.getSubtask(7);
        tm.getEpic(5);
        s = "[" +
                "Task{id=2, name='2', description='description', status=NEW}," +
                " Task{id=1, name='1', description='description', status=NEW}," +
                " Task{id=4, name='4', description='description', status=NEW}," +
                " Epic{subtasksId[], id=8, name='8', description='description', status=NEW}," +
                " Subtask{epicId=5, id=7, name='7', description='description', status=NEW}," +
                " Epic{subtasksId[6, 7], id=5, name='5', description='description', status=NEW}" +
                "]";
        System.out.println("Вызов истории просмотра задач");
        System.out.println(tm.getHistory());
        System.out.print("Ожидаемые id в истории: 2, 1, 4, 8, 7, 5: ");
        System.out.println(tm.getHistory().toString().equals(s));
        System.out.println();

        System.out.println("Удаление эпика 5");
        tm.deleteEpic(5);
        s = "[" +
                "Task{id=2, name='2', description='description', status=NEW}," +
                " Task{id=1, name='1', description='description', status=NEW}," +
                " Task{id=4, name='4', description='description', status=NEW}," +
                " Epic{subtasksId[], id=8, name='8', description='description', status=NEW}" +
                "]";
        System.out.println("Вызов истории просмотра задач");
        System.out.println(tm.getHistory());
        System.out.print("Ожидаемые id в истории: 2, 1, 4, 8: ");
        System.out.println(tm.getHistory().toString().equals(s));
        System.out.println();

        System.out.println("Добавление эпику 8 подзадачи 9 и вызов этой подзадачи");
        /*
        Subtask{epicId=8, id=9, name='9', description='description', status=NEW}
         */
        tm.addSubtask(getNewSubtask("9", 8));
        tm.getSubtask(9);
        s = "[" +
                "Task{id=2, name='2', description='description', status=NEW}," +
                " Task{id=1, name='1', description='description', status=NEW}," +
                " Task{id=4, name='4', description='description', status=NEW}," +
                " Epic{subtasksId[9], id=8, name='8', description='description', status=NEW}," +
                " Subtask{epicId=8, id=9, name='9', description='description', status=NEW}" +
                "]";
        System.out.println("Вызов истории просмотра задач");
        System.out.println(tm.getHistory());
        System.out.print("Ожидаемые id в истории: 2, 1, 4, 8, 9: ");
        System.out.println(tm.getHistory().toString().equals(s));
        System.out.println();
    }

    private static Task getNewTask(String name) {
        return new Task(name, "description", Status.NEW);
    }

    private static Epic getNewEpic(String name) {
        return new Epic(name, "description");
    }

    private static Subtask getNewSubtask(String name, int epicId) {
        return new Subtask(epicId, name, "description", Status.NEW);
    }
}