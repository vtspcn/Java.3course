import java.sql.SQLType;
import java.util.ArrayList;
import java.util.List;

public class QueryConstructor {
    private Entity entity;

    public QueryConstructor(Entity entity) {
        this.entity = entity;
    }

    public String create(){
        return entity.createTableAsk();
    }
    public String drop(){
        return entity.dropTableAsk();
    }

    public String insert(Object object) throws Exception {
        List<String> rows = new ArrayList<>();
        List<Column> columns = entity.getColumns();

        rows.add("INSERT INTO %s VALUES (".formatted(entity.getTableName()));

        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);
            String cond = column.getSqlVal(object);
            if(cond.isEmpty())
                return "";

            rows.add(cond);
            rows.add(",");
        }
        rows.remove(rows.size()-1);

        return Support.toString(rows)+");";
    }

    public String selectAll(){
        return "SELECT * FROM %s;".formatted(entity.getTableName());
    }

    public String select(Object object) throws Exception {
        String ask = "SELECT * FROM %s WHERE ".formatted(entity.getTableName());
        ask+= getFullCondition(object, "and");
        ask+= ";";

        return ask;
    }

    public String delete(Object object) throws Exception {
        String ask = "DELETE FROM %s WHERE ".formatted(entity.getTableName());
        ask+= getFullCondition(object, "and");
        ask+= ";";

        return ask;
    }

    public String update(Object condition, Object set) throws Exception {
        String ask = "UPDATE %s SET ".formatted(entity.getTableName());
        ask+= getFullCondition(set, ",");
        ask+=" WHERE ";
        ask+= getFullCondition(condition, "and");

        return ask+";";
    }
    private String getFullCondition(Object object, String separator) throws Exception {
        List<String> rows = new ArrayList<>();
        List<Column> columns = entity.getColumns();

        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);
            String cond = column.getConditionRow(object);
            if(cond.isEmpty())
                continue;
            rows.add(cond);
            rows.add(separator);
        }

        if (rows.size() == 0)
            return "";

        rows.remove(rows.size()-1);

        return Support.toString(rows);
    }

    public Entity getEntity() {
        return entity;
    }
}
