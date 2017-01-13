package pl.khuzzuk.mtg.organizer;

import pl.khuzzuk.dao.DAO;
import pl.khuzzuk.dao.Named;
import pl.khuzzuk.mtg.organizer.dm.PrimaryType;
import pl.khuzzuk.mtg.organizer.dm.Type;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class DAOFilter {
    private DAO dao;

    DAOFilter(DAO dao) {
        this.dao = dao;
    }

    Collection<Type> filterResults(PrimaryType criteria) {
        if (criteria == null) return Collections.emptyList();
        return dao.getAllEntities(Type.class).stream().filter(t -> t.getPrimaryType() == criteria)
                .sorted((t1, t2) -> t1.getName().compareTo(t2.getName()))
                .collect(Collectors.toList());
    }

    <T extends Named<String>> Collection<T> getFiltered(
            Class<T> type, Collection<Predicate<T>> predicates) {
        Collection<T> entities = dao.getAllEntities(type);
        return entities.stream().filter(e -> shouldAdd(e, predicates)).collect(Collectors.toList());
    }

    private <T> boolean shouldAdd(T element, Collection<Predicate<T>> predicates) {
        boolean isValid = true;
        for (Predicate<T> p : predicates) {
            if (!p.test(element)) {
                isValid = false;
                break;
            }
        }
        return isValid;
    }
}
