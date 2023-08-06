package pl.kurs.shapeapiapp.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import org.springframework.stereotype.Service;
import pl.kurs.shapeapiapp.model.QShape;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class FindShapesQuery {

    private final Set<IFindShapesQuery> shapesQuery;

    public FindShapesQuery(Set<IFindShapesQuery> shapesQuery) {
        this.shapesQuery = shapesQuery;
    }

    public Set<IFindShapesQuery> getShapesQuery() {
        return shapesQuery;
    }

    public Predicate toPredicate(Map<String, String> parameters) {
        BooleanBuilder conditions = new BooleanBuilder();
        for (IFindShapesQuery shapeImplementation : shapesQuery) {
            conditions.and(shapeImplementation.toPredicate(parameters)); // Change 'or' to 'and'
        }
        Optional.ofNullable(parameters.get("type")).map(QShape.shape.type::containsIgnoreCase).ifPresent(conditions::and);
        return conditions;
    }

    public static void checkAreaAndPerimeter(Map<String, String> parameters, BooleanBuilder conditions, NumberExpression<Double> area, NumberExpression<Double> perimeter, BooleanExpression correctShapeType) {
        Optional.ofNullable(parameters.get("areaFrom")).map(x -> area.goe(Double.parseDouble(x)).and(correctShapeType)).ifPresent(conditions::and);
        Optional.ofNullable(parameters.get("areaTo")).map(x -> area.loe(Double.parseDouble(x)).and(correctShapeType)).ifPresent(conditions::and);
        Optional.ofNullable(parameters.get("perimeterFrom")).map(x -> perimeter.goe(Double.parseDouble(x)).and(correctShapeType)).ifPresent(conditions::and);
        Optional.ofNullable(parameters.get("perimeterTo")).map(x -> perimeter.loe(Double.parseDouble(x)).and(correctShapeType)).ifPresent(conditions::and);
    }
}
