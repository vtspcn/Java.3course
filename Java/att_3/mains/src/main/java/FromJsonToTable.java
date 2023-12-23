
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class FromJsonToTable {
    public static void main(String[] args) throws Exception {
        JdbcConn jdbcConn = new JdbcConn();
        List<Project> projects = new JsonReader("projects.json").execute();

        Map<Class, QueryConstructor> queryConstructorMap = Map.of(
                Comment.class, new QueryConstructor(new Entity(Comment.class)),
                Project.class, new QueryConstructor(new Entity(Project.class)),
                Task.class, new QueryConstructor(new Entity(Task.class)),
                Team.class, new QueryConstructor(new Entity(Team.class)),
                User.class, new QueryConstructor(new Entity(User.class))
        );

        for (QueryConstructor queryConstructor: queryConstructorMap.values()) {
            new Query(jdbcConn).setAsk(queryConstructor.drop()).execute();
            new Query(jdbcConn).setAsk(queryConstructor.create()).execute();
        }

        for (Project p: projects) {
            QueryConstructor pq = queryConstructorMap.get(Project.class);
            new Query(jdbcConn).setAsk(pq.insert(p)).execute();

            QueryConstructor tq = queryConstructorMap.get(Team.class);
            new Query(jdbcConn).setAsk(tq.insert(p.getTeam())).execute();

            for (User u: p.getTeam().getUsers()){
                QueryConstructor uq = queryConstructorMap.get(User.class);
                new Query(jdbcConn).setAsk(uq.insert(u)).execute();
            }

            for (Task t: p.getTasks()) {
                QueryConstructor tsq = queryConstructorMap.get(Task.class);
                new Query(jdbcConn).setAsk(tsq.insert(t)).execute();

                QueryConstructor cq = queryConstructorMap.get(Comment.class);
                new Query(jdbcConn).setAsk(cq.insert(t.getComment())).execute();
            }

        }


    }

}
