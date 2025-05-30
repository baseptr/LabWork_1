package lt.esdc.shapes.validator;

@FunctionalInterface
public interface ShapeDataValidator {
    boolean isValid(String dataLine);
}