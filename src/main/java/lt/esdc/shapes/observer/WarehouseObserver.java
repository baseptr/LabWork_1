package lt.esdc.shapes.observer;

import lt.esdc.shapes.entity.AbstractShape;
import lt.esdc.shapes.entity.Oval;
import lt.esdc.shapes.service.OvalService;
import lt.esdc.shapes.warehouse.ShapeWarehouse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WarehouseObserver implements ShapeObserver {
    private static final Logger logger = LogManager.getLogger(WarehouseObserver.class);
    private final OvalService ovalService;


    public WarehouseObserver(OvalService ovalService) {
        if (ovalService == null) {
            throw new IllegalArgumentException("OvalService cannot be null for WarehouseObserver");
        }
        this.ovalService = ovalService;
    }

    @Override
    public void update(AbstractShape shape) {
        if (shape instanceof Oval oval) {
            logger.debug("WarehouseObserver received update for Oval ID: {}", oval.getId());
            ShapeWarehouse warehouse = ShapeWarehouse.getInstance();
            warehouse.updateOvalParameters(oval, ovalService);
        } else {
            logger.warn("WarehouseObserver received update for unhandled shape type: {}",
                    shape.getClass().getSimpleName());
        }
    }
}