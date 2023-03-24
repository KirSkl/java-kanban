package tests;

import manager.InMemoryTaskManager;

import org.junit.jupiter.api.BeforeEach;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    void createNewManager() {
        manager = new InMemoryTaskManager();
    }
}
