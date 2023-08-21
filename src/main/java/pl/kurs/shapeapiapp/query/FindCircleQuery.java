package pl.kurs.shapeapiapp.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import org.springframework.stereotype.Component;
import pl.kurs.shapeapiapp.model.QCircle;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Optional;

@Component
public class FindCircleQuery implements IFindShapesQuery {

    private final QCircle qCircle = new QCircle("shape");

    @Override
    public BooleanBuilder toPredicate(Map<String, String> parameters) {
        BooleanBuilder builder = new BooleanBuilder();
        NumberExpression<Double> area = (qCircle.radius.multiply(qCircle.radius)).multiply(Math.PI);
        NumberExpression<Double> perimeter = (qCircle.radius.multiply(qCircle.radius)).multiply(2 * Math.PI);
        Optional.ofNullable(parameters.get("radiusFrom")).map(x -> qCircle.radius.goe(Double.parseDouble(x))).ifPresent(builder::and);
        Optional.ofNullable(parameters.get("radiusTo")).map(x -> qCircle.radius.loe(Double.parseDouble(x))).ifPresent(builder::and);
        Optional.ofNullable(parameters.get("areaFrom")).map(x -> area.goe(Double.parseDouble(x)).and(qCircle.type.containsIgnoreCase("circle"))).ifPresent(builder::and);
        Optional.ofNullable(parameters.get("areaTo")).map(x -> area.loe(Double.parseDouble(x)).and(qCircle.type.containsIgnoreCase("circle"))).ifPresent(builder::and);
        Optional.ofNullable(parameters.get("perimeterFrom")).map(x -> perimeter.goe(Double.parseDouble(x)).and(qCircle.type.containsIgnoreCase("circle"))).ifPresent(builder::and);
        Optional.ofNullable(parameters.get("perimeterFrom")).map(x -> perimeter.loe(Double.parseDouble(x)).and(qCircle.type.containsIgnoreCase("circle"))).ifPresent(builder::and);
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        try {
            Optional.ofNullable(parameters.get("dateFrom"))
                    .map(x -> LocalDateTime.parse(x, inputFormatter))
                    .ifPresent(dateFrom -> builder.and(qCircle.createdAt.goe(dateFrom)));

            Optional.ofNullable(parameters.get("dateTo"))
                    .map(x -> LocalDateTime.parse(x, inputFormatter))
                    .ifPresent(dateTo -> builder.and(qCircle.createdAt.loe(dateTo)));
        } catch (DateTimeParseException e) {
            e.printStackTrace();
        }

        return builder;
    }
}
