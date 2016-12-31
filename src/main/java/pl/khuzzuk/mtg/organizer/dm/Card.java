package pl.khuzzuk.mtg.organizer.dm;

import org.hibernate.annotations.NaturalId;
import pl.khuzzuk.dao.Named;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Card implements Named<String> {
    @Id
    @GeneratedValue
    private long id;
    @NaturalId
    private String name;
    private byte white;
    private byte green;
    private byte blue;
    private byte red;
    private byte black;
    private byte colorless;
    @ManyToOne(fetch = FetchType.EAGER)
    private Edition edition;
    private String signature;
    private CardRarity rarity;
    private PrimaryType primaryType;
    @ManyToMany
    private Set<Type> types;
    private int picId;
    private int revPicId;

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

    public byte getWhite() {
        return white;
    }

    public void setWhite(byte white) {
        this.white = white;
    }

    public byte getGreen() {
        return green;
    }

    public void setGreen(byte green) {
        this.green = green;
    }

    public byte getBlue() {
        return blue;
    }

    public void setBlue(byte blue) {
        this.blue = blue;
    }

    public byte getRed() {
        return red;
    }

    public void setRed(byte red) {
        this.red = red;
    }

    public byte getBlack() {
        return black;
    }

    public void setBlack(byte black) {
        this.black = black;
    }

    public byte getColorless() {
        return colorless;
    }

    public void setColorless(byte colorless) {
        this.colorless = colorless;
    }

    public Edition getEdition() {
        return edition;
    }

    public void setEdition(Edition edition) {
        this.edition = edition;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public CardRarity getRarity() {
        return rarity;
    }

    public void setRarity(CardRarity rarity) {
        this.rarity = rarity;
    }

    public PrimaryType getPrimaryType() {
        return primaryType;
    }

    public void setPrimaryType(PrimaryType primaryType) {
        this.primaryType = primaryType;
    }

    public Set<Type> getTypes() {
        return types;
    }

    public void addType(Type type) {
        if (types == null) {
            types = new HashSet<>();
        }
        types.add(type);
    }

    public void setTypes(Set<Type> types) {
        this.types = types;
    }

    public int getPicId() {
        return picId;
    }

    public void setPicId(int picId) {
        this.picId = picId;
    }

    public int getRevPicId() {
        return revPicId;
    }

    public void setRevPicId(int revPicId) {
        this.revPicId = revPicId;
    }

    @Override
    public String toString() {
        return getName();
    }
}
