package com.lab.pool;

import com.lab.entity.Oval;
import com.lab.service.OvalService;

public class ShapeWarehouse {
    private static final ShapeWarehouse INSTANCE = new ShapeWarehouse();
    private final OvalService service = new OvalService();

    private ShapeWarehouse() {
    }

    public static ShapeWarehouse getInstance() {
        return INSTANCE;
    }

    public void update(Oval oval) {
    // Сохранение периметра и площади
    }

}
