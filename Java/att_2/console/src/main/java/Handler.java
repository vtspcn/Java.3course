import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Handler {
    private JdbcConn jdbcConn;
    private List<QueryConstructor> queryConstructors;

    public Handler(JdbcConn jdbcConn, List<QueryConstructor> queryConstructors) {
        this.jdbcConn = jdbcConn;
        this.queryConstructors = queryConstructors;
    }


    public void readAll() throws Exception {

        while (true){
            System.out.print("Command: ");
            String command = read();
            if (command.equals("exit"))
                return;

            executeCommand(command);
        }

    }

    private void executeCommand(String command) throws Exception {
        System.out.print("Table Name: ");
        String table = read();

        QueryConstructor queryConstructor = getByTable(table);

        if (queryConstructor == null) {
            System.out.println("No table found");
            return;
        }

        if (command.equals("all")){
            for (Object o:Deserializer.fromResultSetAll(
                    new Query(jdbcConn).setAsk(queryConstructor.selectAll()).executeQuery(),
                    queryConstructor.getEntity())) {
                System.out.println(o);
            }
            System.out.println();
        }else if (command.equals("select")){
            Object object = readObj(queryConstructor.getEntity());

            for (Object o:Deserializer.fromResultSetAll(
                    new Query(jdbcConn).setAsk(queryConstructor.select(object)).executeQuery(),
                    queryConstructor.getEntity())) {
                System.out.println(o);
            }
            System.out.println();
        }else if (command.equals("delete")){
            Object object = readObj(queryConstructor.getEntity());
            new Query(jdbcConn).setAsk(queryConstructor.delete(object)).execute();
        }else if(command.equals("update")){
            System.out.println("Condition");
            Object condition = readObj(queryConstructor.getEntity());
            System.out.println("Set");
            Object set = readObj(queryConstructor.getEntity());
            new Query(jdbcConn).setAsk(queryConstructor.update(condition, set)).execute();
        }else if (command.equals("insert")){
            Object object = readObj(queryConstructor.getEntity());
            new Query(jdbcConn).setAsk(queryConstructor.insert(object)).execute();
        }else {
            System.out.println("no command found");
        }
    }


    private QueryConstructor getByTable(String table){

        for (QueryConstructor q : queryConstructors) {
            if(q.getEntity().getTableName().equals(table)){
                return q;
            }
        }
        return null;
    }

    private String read(){
        return new Scanner(System.in).nextLine().trim();
    }

    private Object readObj(Entity entity) throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        Object object = entity.getSourceClass().newInstance();
        for (Column c: entity.getColumns()) {
            System.out.print(c.getColumnName()+": ");
            String val = read();
            if (val.isEmpty())
                continue;

            c.setFieldValue(object, val);
        }
        System.out.println(object);
        return object;
    }
}
