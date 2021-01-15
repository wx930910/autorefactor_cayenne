package org.apache.cayenne.testdo.relationships_to_one_fk.auto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.cayenne.BaseDataObject;
import org.apache.cayenne.exp.Property;
import org.apache.cayenne.testdo.relationships_to_one_fk.ToOneFK2;

/**
 * Class _ToOneFK1 was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _ToOneFK1 extends BaseDataObject {

    private static final long serialVersionUID = 1L; 

    public static final String TO_ONE_FK1_PK_PK_COLUMN = "TO_ONE_FK1_PK";

    public static final Property<ToOneFK2> TO_PK = Property.create("toPK", ToOneFK2.class);


    protected Object toPK;

    public void setToPK(ToOneFK2 toPK) {
        setToOneTarget("toPK", toPK, true);
    }

    public ToOneFK2 getToPK() {
        return (ToOneFK2)readProperty("toPK");
    }

    @Override
    public Object readPropertyDirectly(String propName) {
        if(propName == null) {
            throw new IllegalArgumentException();
        }

        switch(propName) {
            case "toPK":
                return this.toPK;
            default:
                return super.readPropertyDirectly(propName);
        }
    }

    @Override
    public void writePropertyDirectly(String propName, Object val) {
        if(propName == null) {
            throw new IllegalArgumentException();
        }

        switch (propName) {
            case "toPK":
                this.toPK = val;
                break;
            default:
                super.writePropertyDirectly(propName, val);
        }
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        writeSerialized(out);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        readSerialized(in);
    }

    @Override
    protected void writeState(ObjectOutputStream out) throws IOException {
        super.writeState(out);
        out.writeObject(this.toPK);
    }

    @Override
    protected void readState(ObjectInputStream in) throws IOException, ClassNotFoundException {
        super.readState(in);
        this.toPK = in.readObject();
    }

}
