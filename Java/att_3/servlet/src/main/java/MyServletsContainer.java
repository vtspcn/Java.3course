import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;


import java.util.List;

public class MyServletsContainer {

    private static final int PORT = 8080;
    private JdbcConn jdbcConn;
    public MyServletsContainer(JdbcConn jdbcConn) {
        this.jdbcConn = jdbcConn;
    }


    public void start(List<QueryConstructor> queryConstructors){
        Server server = new Server(PORT);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        for (QueryConstructor q:queryConstructors) {
            context.addServlet(
                    new ServletHolder(
                            new MyServlet(
                                    jdbcConn,
                                    q
                            )
                    ),
                    "/%s".formatted(q.getEntity().getTableName())
            );
        }



        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { context });
        server.setHandler(handlers);

        try {
            server.start();
            System.out.println("Listening port : " + PORT );

            server.join();
        } catch (Exception e) {
            System.out.println("Error.");
            e.printStackTrace();
        }
    }


}