package choi.dimigo.address;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class TableRowDataModel {
	private StringProperty kind;
    private StringProperty contents;
 
    public TableRowDataModel(StringProperty kind, StringProperty contents) {
        this.kind = kind;
        this.contents = contents;
    }
 
    public StringProperty kindProperty() {
        return kind;
    }
    public StringProperty contentsProperty() {
        return contents;
    }
}
