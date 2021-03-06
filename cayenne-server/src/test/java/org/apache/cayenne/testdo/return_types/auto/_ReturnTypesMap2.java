package org.apache.cayenne.testdo.return_types.auto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.cayenne.CayenneDataObject;
import org.apache.cayenne.exp.Property;

/**
 * Class _ReturnTypesMap2 was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _ReturnTypesMap2 extends CayenneDataObject {

    private static final long serialVersionUID = 1L; 

    public static final String AAAID_PK_COLUMN = "AAAID";

    public static final Property<byte[]> BINARY_COLUMN = Property.create("binaryColumn", byte[].class);
    public static final Property<byte[]> BLOB_COLUMN = Property.create("blobColumn", byte[].class);
    public static final Property<byte[]> LONGVARBINARY_COLUMN = Property.create("longvarbinaryColumn", byte[].class);
    public static final Property<byte[]> VARBINARY_COLUMN = Property.create("varbinaryColumn", byte[].class);

    protected byte[] binaryColumn;
    protected byte[] blobColumn;
    protected byte[] longvarbinaryColumn;
    protected byte[] varbinaryColumn;


    public void setBinaryColumn(byte[] binaryColumn) {
        beforePropertyWrite("binaryColumn", this.binaryColumn, binaryColumn);
        this.binaryColumn = binaryColumn;
    }

    public byte[] getBinaryColumn() {
        beforePropertyRead("binaryColumn");
        return this.binaryColumn;
    }

    public void setBlobColumn(byte[] blobColumn) {
        beforePropertyWrite("blobColumn", this.blobColumn, blobColumn);
        this.blobColumn = blobColumn;
    }

    public byte[] getBlobColumn() {
        beforePropertyRead("blobColumn");
        return this.blobColumn;
    }

    public void setLongvarbinaryColumn(byte[] longvarbinaryColumn) {
        beforePropertyWrite("longvarbinaryColumn", this.longvarbinaryColumn, longvarbinaryColumn);
        this.longvarbinaryColumn = longvarbinaryColumn;
    }

    public byte[] getLongvarbinaryColumn() {
        beforePropertyRead("longvarbinaryColumn");
        return this.longvarbinaryColumn;
    }

    public void setVarbinaryColumn(byte[] varbinaryColumn) {
        beforePropertyWrite("varbinaryColumn", this.varbinaryColumn, varbinaryColumn);
        this.varbinaryColumn = varbinaryColumn;
    }

    public byte[] getVarbinaryColumn() {
        beforePropertyRead("varbinaryColumn");
        return this.varbinaryColumn;
    }

    @Override
    public Object readPropertyDirectly(String propName) {
        if(propName == null) {
            throw new IllegalArgumentException();
        }

        switch(propName) {
            case "binaryColumn":
                return this.binaryColumn;
            case "blobColumn":
                return this.blobColumn;
            case "longvarbinaryColumn":
                return this.longvarbinaryColumn;
            case "varbinaryColumn":
                return this.varbinaryColumn;
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
            case "binaryColumn":
                this.binaryColumn = (byte[])val;
                break;
            case "blobColumn":
                this.blobColumn = (byte[])val;
                break;
            case "longvarbinaryColumn":
                this.longvarbinaryColumn = (byte[])val;
                break;
            case "varbinaryColumn":
                this.varbinaryColumn = (byte[])val;
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
        out.writeObject(this.binaryColumn);
        out.writeObject(this.blobColumn);
        out.writeObject(this.longvarbinaryColumn);
        out.writeObject(this.varbinaryColumn);
    }

    @Override
    protected void readState(ObjectInputStream in) throws IOException, ClassNotFoundException {
        super.readState(in);
        this.binaryColumn = (byte[])in.readObject();
        this.blobColumn = (byte[])in.readObject();
        this.longvarbinaryColumn = (byte[])in.readObject();
        this.varbinaryColumn = (byte[])in.readObject();
    }

}
