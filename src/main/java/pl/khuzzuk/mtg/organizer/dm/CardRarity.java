package pl.khuzzuk.mtg.organizer.dm;

import java.util.EnumSet;
import java.util.Set;

public enum CardRarity {
    COMMON,UNCOMMON,RARE,MYTHIC;
    public static final Set<CardRarity> SET = EnumSet.allOf(CardRarity.class);
}
