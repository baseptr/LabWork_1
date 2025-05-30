package lt.esdc.shapes.specifications;

import lt.esdc.shapes.entity.Oval;
import lt.esdc.shapes.service.OvalService;

public class IsCircle implements Specification<Oval> {
    private final OvalService ovalService;

    public IsCircle(OvalService ovalService) {
        this.ovalService = ovalService;
    }

    @Override
    public boolean isSatisfiedBy(Oval oval) {
        if (oval == null) return false;
        return ovalService.isCircle(oval);
    }
}
