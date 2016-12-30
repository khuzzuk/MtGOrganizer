package pl.khuzzuk.mtg.organizer.dm;

import org.hibernate.annotations.NaturalId;
import pl.khuzzuk.dao.Named;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Type implements Named<String> {
    @Id
    @GeneratedValue
    private long id;
    @NaturalId
    private String name;
    private PrimaryType primaryType;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public PrimaryType getPrimaryType() {
        return primaryType;
    }

    public void setPrimaryType(PrimaryType primaryType) {
        this.primaryType = primaryType;
    }

    @Override
    public String toString() {
        return name;
    }
}
