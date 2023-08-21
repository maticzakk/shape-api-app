package pl.kurs.shapeapiapp.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import org.springframework.stereotype.Component;
import pl.kurs.shapeapiapp.model.QRectangle;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Optional;

@Component
public class FindRectangleQuery implements IFindShapesQuery {

    private final QRectangle qRectangle = new QRectangle("shape");

    @Override
    public BooleanBuilder toPredicate(Map<String, String> parameters) {
        BooleanBuilder builder = new BooleanBuilder();
        NumberExpression<Double> area = qRectangle.height.multiply(qRectangle.width);
        NumberExpression<Double> perimeter = qRectangle.height.multiply(2).add(qRectangle.width.multiply(2));
        Optional.ofNullable(parameters.get("createdBy")).map(x -> qRectangle.createdBy.username.eq(x)).ifPresent(builder::and);
        Optional.ofNullable(parameters.get("heightFrom")).map(x -> qRectangle.height.goe(Double.parseDouble(x))).ifPresent(builder::and);
        Optional.ofNullable(parameters.get("heightTo")).map(x -> qRectangle.height.loe(Double.parseDouble(x))).ifPresent(builder::and);
        Optional.ofNullable(parameters.get("widthFrom")).map(x -> qRectangle.width.goe(Double.parseDouble(x))).ifPresent(builder::and);
        Optional.ofNullable(parameters.get("widthTo")).map(x -> qRectangle.width.loe(Double.parseDouble(x))).ifPresent(builder::and);
        Optional.ofNullable(parameters.get("areaFrom")).map(x -> area.goe(Double.parseDouble(x)).and(qRectangle.type.containsIgnoreCase("rectangle"))).ifPresent(builder::and);
        Optional.ofNullable(parameters.get("areaTo")).map(x -> area.loe(Double.parseDouble(x)).and(qRectangle.type.containsIgnoreCase("rectangle"))).ifPresent(builder::and);
        Optional.ofNullable(parameters.get("perimeterFrom")).map(x -> perimeter.goe(Double.parseDouble(x)).and(qRectangle.type.containsIgnoreCase("rectangle"))).ifPresent(builder::and);
        Optional.ofNullable(parameters.get("perimeterTo")).map(x -> perimeter.goe(Double.parseDouble(x)).and(qRectangle.type.containsIgnoreCase("rectangle"))).ifPresent(builder::and);
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        try {
            Optional.ofNullable(parameters.get("dateFrom"))
                    .map(x -> LocalDateTime.parse(x, inputFormatter))
                    .ifPresent(dateFrom -> builder.and(qRectangle.createdAt.goe(dateFrom)));

            Optional.ofNullable(parameters.get("dateTo"))
                    .map(x -> LocalDateTime.parse(x, inputFormatter))
                    .ifPresent(dateTo -> builder.and(qRectangle.createdAt.loe(dateTo)));
        } catch (DateTimeParseException e) {
            e.printStackTrace();
        }

        return builder;
    }
}
