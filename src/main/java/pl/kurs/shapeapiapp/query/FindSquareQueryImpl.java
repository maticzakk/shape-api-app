package pl.kurs.shapeapiapp.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import org.springframework.stereotype.Component;
import pl.kurs.shapeapiapp.model.QSquare;

import java.util.Map;
import java.util.Optional;

@Component
public class FindSquareQueryImpl implements IFindShapesQuery {
    private final QSquare qSquare = new QSquare("shape");

    public QSquare getqSquare() {
        return qSquare;
    }

    @Override
    public BooleanBuilder toPredicate(Map<String, String> parameters) {
        BooleanBuilder conditions = new BooleanBuilder();
        NumberExpression<Double> area = qSquare.height.multiply(qSquare.height);
        NumberExpression<Double> perimeter = qSquare.height.multiply(4);
        Optional.ofNullable(parameters.get("widthFrom")).map(x -> qSquare.height.goe(Double.parseDouble(x))).ifPresent(conditions::and);
        Optional.ofNullable(parameters.get("widthTo")).map(x -> qSquare.height.loe(Double.parseDouble(x))).ifPresent(conditions::and);
        BooleanExpression shapeType = qSquare.type.containsIgnoreCase("square");
        FindShapesQuery.checkAreaAndPerimeter(parameters, conditions, area, perimeter, shapeType);
        return conditions;
    }
}
