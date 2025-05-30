package lt.esdc.shapes.parser;

import lt.esdc.shapes.exception.InvalidShapeException;
import java.util.List;

public interface DataParser<T> {
    List<T> parse(List<String> dataLines) throws InvalidShapeException;
}