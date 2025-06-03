package lt.esdc.shapes.parser;

import lt.esdc.shapes.entity.Point;
import lt.esdc.shapes.exception.InvalidShapeException;
import lt.esdc.shapes.validator.OvalDataValidator;
import lt.esdc.shapes.validator.ShapeDataValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;


public class OvalDataParser {

    private static final Logger logger = LogManager.getLogger(OvalDataParser.class);
    private final ShapeDataValidator validator = new OvalDataValidator();

    public List<ParsedOvalData> parseLines(List<String> lines) {
        List<ParsedOvalData> parsedDataList = new ArrayList<>();
        for (String line : lines) {
            if (validator.isValid(line)) {
                try {
                    ParsedOvalData data = parseSingleLine(line);
                    parsedDataList.add(data);
                } catch (InvalidShapeException | NumberFormatException e) {
                    logger.warn("Miss uncorrected string data: {} - Reason: {}", line, e.getMessage());
                }
            } else {
                logger.warn("Miss string due to validation error: {}", line);
            }
        }
        return parsedDataList;
    }

    public ParsedOvalData parseSingleLine(String dataLine) throws InvalidShapeException {
        if (!validator.isValid(dataLine)) {
            throw new InvalidShapeException("Data string is invalid: " + dataLine);
        }

        String[] parts = dataLine.split(";");
        if (parts.length != 4) {
            throw new InvalidShapeException("Wrong number of parameters in string: " + dataLine + ". Expected 4.");
        }

        try {
            double x1 = Double.parseDouble(parts[0].trim());
            double y1 = Double.parseDouble(parts[1].trim());
            double x2 = Double.parseDouble(parts[2].trim());
            double y2 = Double.parseDouble(parts[3].trim());

            Point p1 = new Point(x1, y1);
            Point p2 = new Point(x2, y2);

            return new ParsedOvalData(p1, p2);

        } catch (NumberFormatException e) {
            throw new InvalidShapeException("Numeric parsing error in string: " + dataLine, e);
        }
    }

    public record ParsedOvalData(Point p1, Point p2) {
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("ParsedOvalData {");
            sb.append("p1 = ").append(p1).append(", ");
            sb.append("p2 = ").append(p2).append("}");
            return sb.toString();
        }
    }
}