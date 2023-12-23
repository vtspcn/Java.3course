import java.sql.ResultSet;
import java.sql.SQLException;

public class Query {
    private String ask;
    private JdbcConn conn;
    public Query(JdbcConn conn) {
        this.conn = conn;
    }

    public Query setAsk(String ask){
        this.ask = ask;
        System.out.println(ask);
        return this;
    }

    public ResultSet executeQuery() throws SQLException {
        return conn.getConnection().createStatement().executeQuery(ask);
    }
    public void execute() throws SQLException {
        conn.getConnection().createStatement().execute(ask);
    }

}
