package pl.kurs.shapeapiapp.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Service;
import pl.kurs.shapeapiapp.model.QShape;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class FindShapesQuery {

    private final Set<IFindShapesQuery> shapesQuery;

    public FindShapesQuery(Set<IFindShapesQuery> allShapesQuery) {
        shapesQuery = allShapesQuery;
    }

    public Predicate toPredicate(Map<String, String> parameters){
        BooleanBuilder conditions = new BooleanBuilder();
        for (IFindShapesQuery shapeImplementation : shapesQuery) {
            conditions.or(shapeImplementation.toPredicate(parameters));
        }
        Optional.ofNullable(parameters.get("type")).map(QShape.shape.type::containsIgnoreCase).ifPresent(conditions::and);
        return conditions;
    }
}
