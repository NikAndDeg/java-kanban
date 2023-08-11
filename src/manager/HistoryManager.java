package manager;

import java.util.List;

public interface HistoryManager<T> {
	public void add(T t);
	public List<T> getHistory();
}
