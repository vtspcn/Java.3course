import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ConsoleMain {
    public static void main(String[] args) throws Exception {
        JdbcConn jdbcConn = new JdbcConn();

        List<QueryConstructor> queryConstructors = List.of(
                new QueryConstructor(new Entity(Comment.class)),
                new QueryConstructor(new Entity(Project.class)),
                new QueryConstructor(new Entity(Task.class)),
                new QueryConstructor(new Entity(Team.class)),
                new QueryConstructor(new Entity(User.class))
        );
        Handler handler = new Handler(
                jdbcConn,
                queryConstructors
        );
        handler.readAll();
    }
}
