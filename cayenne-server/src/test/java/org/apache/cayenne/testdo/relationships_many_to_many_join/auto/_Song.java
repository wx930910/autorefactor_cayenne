package org.apache.cayenne.testdo.relationships_many_to_many_join.auto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Set;

import org.apache.cayenne.BaseDataObject;
import org.apache.cayenne.exp.Property;
import org.apache.cayenne.testdo.relationships_many_to_many_join.Author;

/**
 * Class _Song was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _Song extends BaseDataObject {

    private static final long serialVersionUID = 1L; 

    public static final String SONG_ID_PK_COLUMN = "SONG_ID";

    public static final Property<String> NAME = Property.create("name", String.class);
    public static final Property<Set<Author>> AUTHORS = Property.create("authors", Set.class);

    protected String name;

    protected Object authors;

    public void setName(String name) {
        beforePropertyWrite("name", this.name, name);
        this.name = name;
    }

    public String getName() {
        beforePropertyRead("name");
        return this.name;
    }

    public void addToAuthors(Author obj) {
        addToManyTarget("authors", obj, true);
    }

    public void removeFromAuthors(Author obj) {
        removeToManyTarget("authors", obj, true);
    }

    @SuppressWarnings("unchecked")
    public Set<Author> getAuthors() {
        return (Set<Author>)readProperty("authors");
    }

    @Override
    public Object readPropertyDirectly(String propName) {
        if(propName == null) {
            throw new IllegalArgumentException();
        }

        switch(propName) {
            case "name":
                return this.name;
            case "authors":
                return this.authors;
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
            case "name":
                this.name = (String)val;
                break;
            case "authors":
                this.authors = val;
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
        out.writeObject(this.name);
        out.writeObject(this.authors);
    }

    @Override
    protected void readState(ObjectInputStream in) throws IOException, ClassNotFoundException {
        super.readState(in);
        this.name = (String)in.readObject();
        this.authors = in.readObject();
    }

}
