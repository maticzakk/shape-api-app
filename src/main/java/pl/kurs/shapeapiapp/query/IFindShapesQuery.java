package pl.kurs.shapeapiapp.query;

import com.querydsl.core.BooleanBuilder;

import java.util.Map;

public interface IFindShapesQuery {
    BooleanBuilder toPredicate(Map<String, String> parameters);
}
