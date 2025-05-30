package lt.esdc.shapes.reader;

import lt.esdc.shapes.entity.Oval;
import lt.esdc.shapes.exception.InvalidShapeException;
import lt.esdc.shapes.factory.OvalFactory;
import lt.esdc.shapes.parser.OvalDataParser;
import lt.esdc.shapes.parser.OvalDataParser.ParsedOvalData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ShapeFileReader {
    private static final Logger logger = LogManager.getLogger(ShapeFileReader.class);
    private final OvalDataParser ovalParser;
    private final OvalFactory ovalFactory;

    public ShapeFileReader() {
        this.ovalParser = new OvalDataParser();
        this.ovalFactory = new OvalFactory();
    }

    public List<Oval> readOvalsFromFile(String filePathString) {
        Path filePath = Paths.get(filePathString);
        if (!Files.exists(filePath) || !Files.isReadable(filePath)) {
            logger.error("File doesn't exist or not allowed for read: {}", filePathString);
            return new ArrayList<>();
        }

        List<String> lines;
        try (Stream<String> stream = Files.lines(filePath)) {
            lines = stream.collect(Collectors.toList());
        } catch (IOException e) {
            logger.error("File reading error{}: {}", filePathString, e.getMessage(), e);
            return new ArrayList<>();
        }

        List<ParsedOvalData> parsedDataItems = ovalParser.parseLines(lines);
        List<Oval> ovals = new ArrayList<>();

        for (ParsedOvalData data : parsedDataItems) {
            try {
                Oval oval = ovalFactory.createShape(data.p1(), data.p2());
                ovals.add(oval);
                logger.debug("Oval successfully created: P1={}, P2={}. Assigned ID: {}", data.p1(), data.p2(), oval.getId());
            } catch (InvalidShapeException e) {
                logger.warn("Don't able to create Oval from parsed data (P1 {}, P2 {}): {}. Passed.", data.p1(), data.p2(), e.getMessage());
            }
        }
        logger.info("Successfully read and created {} ovals from file {}.", ovals.size(), filePathString);
        return ovals;
    }
}