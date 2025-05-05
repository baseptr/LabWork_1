package It.esdc.shapes.specifications;

import It.esdc.shapes.entity.Oval;
import It.esdc.shapes.service.OvalService;

public class PerimeterLessThanTwenty implements Specification<Oval> {
    private final OvalService service = new OvalService();

    @Override
    public boolean isSatisfiedBy(Oval oval) {
        return service.calculatePerimeter(oval) < 20;
    }
}