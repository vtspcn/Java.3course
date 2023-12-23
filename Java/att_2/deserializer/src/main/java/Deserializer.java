
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Deserializer {

    public static Object fromResultSetOne(ResultSet resultSet, Entity entity) throws InstantiationException, IllegalAccessException, SQLException, NoSuchFieldException {

        Object object = entity.getSourceClass().newInstance();
        for (Column c: entity.getColumns()) {
            c.setFieldValue(object,
                    resultSet.getObject(c.getColumnName())
            );
        }

        return object;
    }

    public static List<Object> fromResultSetAll(ResultSet resultSet, Entity entity) throws Exception {
        List<Object> objects = new ArrayList<>();
        while (resultSet.next()){
            objects.add(
                    fromResultSetOne(resultSet, entity)
            );
        }
        return objects;
    }

}
