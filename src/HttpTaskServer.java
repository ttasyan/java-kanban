import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

import managers.InMemoryTaskManager;
import managers.TaskManager;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static HttpServer server;
    public static Gson gson;


    TaskManager manager;

    public HttpTaskServer(TaskManager manager) {
        this.manager = manager;
    }

    public static void main(String[] args) throws IOException {
        TaskManager manager = new InMemoryTaskManager();
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", new TaskHandler(manager));
        server.createContext("/epics", new EpicHandler(manager));
        server.createContext("/subtasks", new SubTaskHandler(manager));
        server.createContext("/history", new HistoryHandler(manager));
        server.createContext("/prioritized", new PrioritizedHandler(manager));
        server.start();
        gson = new Gson();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
        server.start();
    }

    public void start() throws IOException {
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("HTTP-сервер остановлен.");
    }

}
