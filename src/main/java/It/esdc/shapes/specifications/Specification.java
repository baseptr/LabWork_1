package It.esdc.shapes.specifications;

public interface Specification<T>{
    boolean isSatisfiedBy(T oval);
}
