package lt.esdc.shapes.repository;

import lt.esdc.shapes.entity.Oval;
import lt.esdc.shapes.observer.ShapeObserver;
import lt.esdc.shapes.observer.WarehouseObserver;
import lt.esdc.shapes.service.OvalService;
import lt.esdc.shapes.specifications.Specification;

import lt.esdc.shapes.warehouse.ShapeWarehouse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ShapeRepository {
    private static final Logger logger = LogManager.getLogger(ShapeRepository.class);
    private static ShapeRepository instance;
    private final List<Oval> ovals;
    private final ShapeObserver warehouseObserver;
    private final ShapeWarehouse warehouse;

    private ShapeRepository(OvalService ovalService) {
        this.ovals = new ArrayList<>();
        this.warehouseObserver = new WarehouseObserver(ovalService);
        this.warehouse = ShapeWarehouse.getInstance();
    }

    public static ShapeRepository getInstance(OvalService ovalService) {
        if (instance == null) {
            instance = new ShapeRepository(ovalService);
        }
        return instance;
    }
    public static ShapeRepository getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Repository not initialized. Call getInstance(OvalService) first.");
        }
        return instance;
    }

    public void add(Oval oval) {
        if (oval == null) {
            logger.warn("Attempted to add a null oval.");
            return;
        }
        if (ovals.stream().anyMatch(o -> o.getId() == oval.getId())) {
            logger.warn("Oval with ID {} already exists. Not adding.", oval.getId());
            return;
        }
        this.ovals.add(oval);
        oval.addObserver(this.warehouseObserver);
        this.warehouseObserver.update(oval);

        logger.info("Added Oval ID: {} to repository.", oval.getId());
    }

    public void addAll(List<Oval> newOvals) {
        if (newOvals == null) return;
        for (Oval oval : newOvals) {
            add(oval);
        }
    }

    public boolean remove(long ovalId) {
        Optional<Oval> ovalToRemove = getById(ovalId);
        if (ovalToRemove.isPresent()) {
            Oval oval = ovalToRemove.get();
            oval.removeObserver(this.warehouseObserver);
            this.ovals.remove(oval);
            this.warehouse.removeParameters(ovalId);
            logger.info("Removed Oval ID: {} from repository.", ovalId);
            return true;
        }
        logger.warn("Oval ID: {} not found for removal.", ovalId);
        return false;
    }

    public Optional<Oval> getById(long ovalId) {
        return this.ovals.stream()
                .filter(oval -> oval.getId() == ovalId)
                .findFirst();
    }

    public List<Oval> getAll() {
        return new ArrayList<>(this.ovals);
    }

    public List<Oval> query(Specification<Oval> specification) {
        return this.ovals.stream()
                .filter(specification::isSatisfiedBy)
                .collect(Collectors.toList());
    }


    public List<Oval> sortById(boolean ascending) {
        Comparator<Oval> comparator = Comparator.comparingLong(Oval::getId);
        if (!ascending) {
            comparator = comparator.reversed();
        }
        return this.ovals.stream().sorted(comparator).collect(Collectors.toList());
    }

    public List<Oval> sortByArea(OvalService ovalService, boolean ascending) {
        if (ovalService == null) throw new IllegalArgumentException("OvalService cannot be null for sorting by area.");
        Comparator<Oval> comparator = Comparator.comparingDouble(ovalService::calculateArea);
        if (!ascending) {
            comparator = comparator.reversed();
        }
        return this.ovals.stream().sorted(comparator).collect(Collectors.toList());
    }
    public List<Oval> sortByPerimeter(OvalService ovalService, boolean ascending) {
        if (ovalService == null) throw new IllegalArgumentException("OvalService cannot be null for sorting by perimeter.");
        Comparator<Oval> comparator = Comparator.comparingDouble(ovalService::calculatePerimeter);
        if (!ascending) {
            comparator = comparator.reversed();
        }
        return this.ovals.stream().sorted(comparator).collect(Collectors.toList());
    }
}