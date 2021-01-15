package org.apache.cayenne.testdo.testmap.auto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Date;
import java.util.List;

import org.apache.cayenne.BaseDataObject;
import org.apache.cayenne.exp.Property;
import org.apache.cayenne.testdo.testmap.Painting;

/**
 * Class _ROArtist was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _ROArtist extends BaseDataObject {

    private static final long serialVersionUID = 1L; 

    public static final String ARTIST_ID_PK_COLUMN = "ARTIST_ID";

    public static final Property<String> ARTIST_NAME = Property.create("artistName", String.class);
    public static final Property<Date> DATE_OF_BIRTH = Property.create("dateOfBirth", Date.class);
    public static final Property<List<Painting>> PAINTING_ARRAY = Property.create("paintingArray", List.class);

    protected String artistName;
    protected Date dateOfBirth;

    protected Object paintingArray;

    public String getArtistName() {
        beforePropertyRead("artistName");
        return this.artistName;
    }

    public Date getDateOfBirth() {
        beforePropertyRead("dateOfBirth");
        return this.dateOfBirth;
    }

    public void addToPaintingArray(Painting obj) {
        addToManyTarget("paintingArray", obj, true);
    }

    public void removeFromPaintingArray(Painting obj) {
        removeToManyTarget("paintingArray", obj, true);
    }

    @SuppressWarnings("unchecked")
    public List<Painting> getPaintingArray() {
        return (List<Painting>)readProperty("paintingArray");
    }

    @Override
    public Object readPropertyDirectly(String propName) {
        if(propName == null) {
            throw new IllegalArgumentException();
        }

        switch(propName) {
            case "artistName":
                return this.artistName;
            case "dateOfBirth":
                return this.dateOfBirth;
            case "paintingArray":
                return this.paintingArray;
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
            case "artistName":
                this.artistName = (String)val;
                break;
            case "dateOfBirth":
                this.dateOfBirth = (Date)val;
                break;
            case "paintingArray":
                this.paintingArray = val;
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
        out.writeObject(this.artistName);
        out.writeObject(this.dateOfBirth);
        out.writeObject(this.paintingArray);
    }

    @Override
    protected void readState(ObjectInputStream in) throws IOException, ClassNotFoundException {
        super.readState(in);
        this.artistName = (String)in.readObject();
        this.dateOfBirth = (Date)in.readObject();
        this.paintingArray = in.readObject();
    }

}