public final class Managers {
    public static TaskManager getDefaults() {
        return new InMemoryTaskManager();
    }
    private Managers() {}
    private TaskManager getDefault() {
        return ;
    }

}
