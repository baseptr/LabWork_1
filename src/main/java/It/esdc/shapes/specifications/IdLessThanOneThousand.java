package It.esdc.shapes.specifications;

import It.esdc.shapes.entity.Oval;

public class IdLessThanOneThousand implements Specification<Oval> {
    @Override
    public boolean isSatisfiedBy(Oval oval) {
        return oval.getId() < 1000;
    }
}