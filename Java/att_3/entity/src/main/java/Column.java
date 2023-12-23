import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;

@Getter
@Setter
public class Column {
    private String columnName;
    private String defultName;
    private Class type;
    private String modifier;
    private boolean isPK;

    public Column(String columnName, String defultName, Class type, String modifier, boolean isPK) {
        this.columnName = columnName;
        this.defultName = defultName;
        this.type = type;
        this.modifier = modifier;
        this.isPK = isPK;
    }

    public Column(Field f) {
        this.columnName = f.getName();
        this.defultName = f.getName();
        this.type = f.getType();
        this.modifier = null;
        this.isPK = f.getName().equals("id");
    }

    public Column(Field f, boolean isPK) {
        this.columnName = f.getName();
        this.defultName = f.getName();
        this.type = f.getType();
        this.modifier = null;
        this.isPK = isPK;
    }

    public String getTableRow(){
        SqlTypes sqlTypes = new SqlTypes();
        return "%s %s %s".formatted(
               columnName,
                sqlTypes.getType(type),
                (isPK)?"PRIMARY KEY":""
        );
    }

    public String getConditionRow(Object object) throws Exception {
        Object value = getVal(object);
        if (value == null)
            return "";

        return "%s = %s".formatted(
                columnName,
                getSqlVal(value)
        );

    }
    public String getSqlVal(Object object) throws Exception {
        Object value = getVal(object);
        if (value == null)
            return "";

        return type.equals(String.class)? "\'%s\'".formatted(value.toString()) : value.toString();

    }

    public Object getVal(Object object) throws Exception {
        return object.getClass().getMethod(
                "get"+capitalizeFirstLetter(getDefultName())
        ).invoke(object);
    }
    private String capitalizeFirstLetter(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public void setFieldValue(Object object, Object value) throws NoSuchFieldException, IllegalAccessException {
        value = autoCast(value);

        Field field = object.getClass().getDeclaredField(getDefultName());
        field.setAccessible(true);
        field.set(object, value);
    }

    private Object autoCast(Object value){
        if (value.getClass().equals(type))
            return value;
        else if(type.equals(Integer.class) || type.equals(int.class)){
            return Integer.parseInt(value.toString());
        }else if (type.equals(Long.class) || type.equals(long.class)){
            return Long.parseLong(value.toString());
        }
        return null;
    }
}
