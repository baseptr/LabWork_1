package lt.esdc.shapes.warehouse;

import lt.esdc.shapes.entity.Oval;
import lt.esdc.shapes.service.OvalService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class ShapeWarehouse {
    private static final Logger logger = LogManager.getLogger(ShapeWarehouse.class);
    private static ShapeWarehouse instance;
    private final Map<Long, ShapeParameters> parametersMap;

    private ShapeWarehouse() {
        parametersMap = new HashMap<>();
    }

    public static ShapeWarehouse getInstance() {
        if (instance == null) {
            instance = new ShapeWarehouse();
        }
        return instance;
    }

    public void putParameters(long ovalId, ShapeParameters parameters) {
        parametersMap.put(ovalId, parameters);
        logger.debug("Stored/Updated parameters for oval ID {}: {}", ovalId, parameters);
    }

    public Optional<ShapeParameters> getParameters(long ovalId) {
        return Optional.ofNullable(parametersMap.get(ovalId));
    }

    public void removeParameters(long ovalId) {
        ShapeParameters removed = parametersMap.remove(ovalId);
        if (removed != null) {
            logger.debug("Removed parameters for oval ID {}", ovalId);
        }
    }

    public Map<Long, ShapeParameters> getAllParameters() {
        return new HashMap<>(parametersMap);
    }


    public void updateOvalParameters(Oval oval, OvalService ovalService) {
        if (oval == null || ovalService == null) {
            logger.warn("Cannot update parameters: Oval or OvalService is null.");
            return;
        }
        long id = oval.getId();
        double area = ovalService.calculateArea(oval);
        double perimeter = ovalService.calculatePerimeter(oval);
        ShapeParameters newParams = new ShapeParameters(area, perimeter);
        putParameters(id, newParams);
        logger.info("Recalculated and updated parameters for Oval ID {}: Area={}, Perimeter={}", id, area, perimeter);
    }

    public record ShapeParameters(double area, double perimeter) {
        @Override
        public String toString() {
            StringBuilder bd = new StringBuilder();
            bd.append("ShapeParameters {")
                    .append("area = ")
                    .append(area)
                    .append(", perimeter = ")
                    .append(perimeter)
                    .append("}");
            return bd.toString();
        }
    }
}