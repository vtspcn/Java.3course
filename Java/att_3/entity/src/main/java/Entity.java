import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Entity {
    private String tableName;
    private Class sourceClass;
    private List<Column> columns;

    public Entity(String tableName, Class sourceClass, List<Column> columns) {
        this.tableName = tableName;
        this.sourceClass = sourceClass;
        this.columns = columns;
    }

    public Entity(Class sourceClass) {
        this.tableName = sourceClass.getSimpleName().toLowerCase()+"s";
        this.sourceClass = sourceClass;
        this.columns = getColumns(sourceClass);
    }
    public Entity(Class sourceClass, List<Column> columns) {
        this.tableName = sourceClass.getSimpleName().toLowerCase();
        this.sourceClass = sourceClass;
        this.columns = columns;
    }
    /*public Entity(Object o) {
        this.tableName = o.getClass().getSimpleName().toLowerCase();
        this.sourceClass = o.getClass();
        this.columns = ;
    }*/

    public String createTableAsk(){
        StringBuilder ask = new StringBuilder("CREATE TABLE IF NOT EXISTS %s (".formatted(tableName));
        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);
            ask.append(column.getTableRow());
            ask.append(
                    i < columns.size()-1 ? ", ":");"
            );
        }
        return ask.toString();
    }
    public String dropTableAsk(){
        return "DROP TABLE IF EXISTS %s;".formatted(tableName);
    }

    private Column getColumn(Field field){
        return new Column(
                field
        );
    }

    private List<Column> getColumns(Class clz){
        SqlTypes sqlTypes = new SqlTypes();
        Field[] fs = clz.getDeclaredFields();

        List<Column> columns = new ArrayList<>();
        for (Field f: clz.getDeclaredFields()) {
            String type = sqlTypes.getType(f.getType());
            if (type != null){
                columns.add(
                        getColumn(f)
                );
            }
        }
        return columns;
    }


}
