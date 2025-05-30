package lt.esdc.shapes.specifications;

import lt.esdc.shapes.entity.Oval;

public class IdLessThanOneThousand implements Specification<Oval> {
    private final long threshold = 1000L;
    @Override
    public boolean isSatisfiedBy(Oval oval) {
        if (oval == null) return false;
        return oval.getId() < threshold;
    }
}