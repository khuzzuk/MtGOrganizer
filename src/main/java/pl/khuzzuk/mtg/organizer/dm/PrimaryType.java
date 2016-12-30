package pl.khuzzuk.mtg.organizer.dm;

import java.util.EnumSet;
import java.util.Set;

public enum PrimaryType {
    CREATURE, ENCHANTMENT, SORCERY, INSTANT, ARTIFACT, LAND;
    public static final Set<PrimaryType> SET = EnumSet.allOf(PrimaryType.class);
}
