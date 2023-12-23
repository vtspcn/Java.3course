import lombok.SneakyThrows;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/Servlet")
public class MyServlet extends HttpServlet {

    private JdbcConn jdbcConn;
    private QueryConstructor queryConstructor;

    public MyServlet(JdbcConn jdbcConn, QueryConstructor queryConstructor) {
        this.jdbcConn = jdbcConn;
        this.queryConstructor = queryConstructor;
    }

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        Object object = readObj(queryConstructor.getEntity(), request);

        ResultSet resultSet;
        if(object == null)
            resultSet = new Query(jdbcConn).setAsk(
                    queryConstructor.selectAll()
            ).executeQuery();
        else
            resultSet = new Query(jdbcConn).setAsk(
                    queryConstructor.select(object)
            ).executeQuery();


        List<Object> objects = Deserializer.fromResultSetAll(resultSet, queryConstructor.getEntity());

        for (Object o:objects) {
            response.getWriter().println(o.toString());
        }
    }
    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object object = readObj(queryConstructor.getEntity(), request);
        new Query(jdbcConn).setAsk(
                queryConstructor.insert(object)
        ).execute();
    }
    @SneakyThrows
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object object = readObj(queryConstructor.getEntity(), request);
        new Query(jdbcConn).setAsk(
                queryConstructor.delete(object)
        ).execute();
    }
    @SneakyThrows
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object condition = readObj(queryConstructor.getEntity(), request, "c");
        Object set = readObj(queryConstructor.getEntity(), request, "s");

        new Query(jdbcConn).setAsk(
                queryConstructor.update(condition, set)
        ).execute();
    }

    private Object readObj(Entity entity, HttpServletRequest request, String prefix) throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        int count = 0;
        Object object = entity.getSourceClass().newInstance();
        for (Column c: entity.getColumns()) {
            String val = request.getParameter(prefix+c.getColumnName());
            if (val.isEmpty())
                continue;
            c.setFieldValue(object, val);
            count+=1;
        }
        return count!=0? object:null;
    }

    private Object readObj(Entity entity, HttpServletRequest request) throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        return readObj(entity, request, "");
    }

}