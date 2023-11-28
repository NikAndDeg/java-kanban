package test;

import manager.InMemoryTaskManager;

import org.junit.jupiter.api.BeforeEach;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
	@BeforeEach
	public void createNewManager() {
		manager = new InMemoryTaskManager();
	}

}