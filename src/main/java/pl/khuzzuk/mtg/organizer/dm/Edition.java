package pl.khuzzuk.mtg.organizer.dm;

import org.hibernate.annotations.NaturalId;
import pl.khuzzuk.dao.Named;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Edition implements Named<String> {
    @Id
    @GeneratedValue
    private long id;
    @NaturalId
    private String name;
    private int year;
    private int picId;

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

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getPicId() {
        return picId;
    }

    public void setPicId(int picId) {
        this.picId = picId;
    }

    @Override
    public String toString() {
        return getName();
    }
}
