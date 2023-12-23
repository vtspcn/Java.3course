import java.util.List;

public class ServletMain {
    public static void main(String[] args) {
        JdbcConn jdbcConn = new JdbcConn();

        List<QueryConstructor> queryConstructors = List.of(
                new QueryConstructor(new Entity(Comment.class)),
                new QueryConstructor(new Entity(Project.class)),
                new QueryConstructor(new Entity(Task.class)),
                new QueryConstructor(new Entity(Team.class)),
                new QueryConstructor(new Entity(User.class))
        );

        MyServletsContainer myServletsContainer = new MyServletsContainer(jdbcConn);
        myServletsContainer.start(queryConstructors);

    }
}
