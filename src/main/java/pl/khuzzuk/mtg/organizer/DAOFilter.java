package pl.khuzzuk.mtg.organizer;

import pl.khuzzuk.dao.DAO;
import pl.khuzzuk.mtg.organizer.dm.PrimaryType;
import pl.khuzzuk.mtg.organizer.dm.Type;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class DAOFilter {
    private DAO dao;

    public DAOFilter(DAO dao) {
        this.dao = dao;
    }

    public Collection<Type> filterResults(PrimaryType criteria) {
        if (criteria == null) return Collections.emptyList();
        return dao.getAllEntities(Type.class).stream().filter(t -> t.getPrimaryType() == criteria)
                .sorted((t1, t2) -> t1.getName().compareTo(t2.getName()))
                .collect(Collectors.toList());
    }
}
