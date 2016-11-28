package gov.goias.persistencia;

import java.util.List;
import java.util.Map;

public interface IDao<Id> {
    <T> void save();
    <T> T get(Id id);
    <T> void update();
    <T> void delete();
    <T> List<T> list();
    <T> List<T> list(Map<String, Object> data, String orderBy);
    <T> List<T> list(Integer offset, Integer max);
    Long count();
}