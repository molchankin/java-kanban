import impl.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
    }
}
