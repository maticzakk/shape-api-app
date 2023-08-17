package pl.kurs.shapeapiapp.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import org.springframework.stereotype.Component;
import pl.kurs.shapeapiapp.model.QSquare;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Optional;
@Component
public class FindSquareQuery implements IFindShapesQuery {

    private final QSquare qSquare = new QSquare("shape");

    @Override
    public BooleanBuilder toPredicate(Map<String, String> parameters) {
        BooleanBuilder builder = new BooleanBuilder();
        NumberExpression<Double> area = qSquare.height.multiply(qSquare.height);
        NumberExpression<Double> perimeter = qSquare.height.multiply(4);
        Optional.ofNullable(parameters.get("createdBy")).map(x -> qSquare.createdBy.username.eq(x)).ifPresent(builder::and);
        Optional.ofNullable(parameters.get("heightFrom")).map(x -> qSquare.height.goe(Double.parseDouble(x))).ifPresent(builder::and);
        Optional.ofNullable(parameters.get("heightTo")).map(x -> qSquare.height.loe(Double.parseDouble(x))).ifPresent(builder::and);
        Optional.ofNullable(parameters.get("areaFrom")).map(x -> area.goe(Double.parseDouble(x)).and(qSquare.type.containsIgnoreCase("square"))).ifPresent(builder::and);
        Optional.ofNullable(parameters.get("areaTo")).map(x -> area.loe(Double.parseDouble(x)).and(qSquare.type.containsIgnoreCase("square"))).ifPresent(builder::and);
        Optional.ofNullable(parameters.get("perimeterFrom")).map(x -> perimeter.goe(Double.parseDouble(x)).and(qSquare.type.containsIgnoreCase("square"))).ifPresent(builder::and);
        Optional.ofNullable(parameters.get("perimeterTo")).map(x -> perimeter.goe(Double.parseDouble(x)).and(qSquare.type.containsIgnoreCase("square"))).ifPresent(builder::and);
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        try {
            Optional.ofNullable(parameters.get("dateFrom"))
                    .map(x -> LocalDateTime.parse(x, inputFormatter))
                    .ifPresent(dateFrom -> builder.and(qSquare.createdAt.goe(dateFrom)));

            Optional.ofNullable(parameters.get("dateTo"))
                    .map(x -> LocalDateTime.parse(x, inputFormatter))
                    .ifPresent(dateTo -> builder.and(qSquare.createdAt.loe(dateTo)));
        } catch (DateTimeParseException e) {
            System.out.println("Błąd " + e.getMessage());
        }

        return builder;

    }
}
