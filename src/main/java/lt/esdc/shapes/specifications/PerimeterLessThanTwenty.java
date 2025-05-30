package lt.esdc.shapes.specifications;

import lt.esdc.shapes.entity.Oval;
import lt.esdc.shapes.service.OvalService;

public class PerimeterLessThanTwenty implements Specification<Oval> {
    private final OvalService ovalService;
    private final double threshold = 20.0;

    public PerimeterLessThanTwenty(OvalService ovalService) {
        this.ovalService = ovalService;
    }
    @Override
    public boolean isSatisfiedBy(Oval oval) {
        if (oval == null) return false;
        return ovalService.calculatePerimeter(oval) < threshold;
    }
}