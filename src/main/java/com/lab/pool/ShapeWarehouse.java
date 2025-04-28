package com.lab.pool;

import com.lab.entity.Oval;
import com.lab.service.OvalService;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

@SuppressWarnings("deprecation")
public class ShapeWarehouse implements java.util.Observer{
    private static final ShapeWarehouse INSTANCE = new ShapeWarehouse();
    private final OvalService service = new OvalService();
    private final Map<Long, Double[]> data = new HashMap<>();


    private ShapeWarehouse() {}

    public static ShapeWarehouse getInstance() {
        return INSTANCE;
    }

    public void delete (long id) {
        data.remove(id);
    }

    public void update(Observable obj, Object arg) {
        if (obj instanceof Oval oval) {
            Double[] values = new Double[]{
                    service.calculatePerimeter(oval),
                    service.calculateArea(oval)
            };
            data.put(oval.getId(), values);
        }
    }

    public Double getPerimeter(long id) {
        Double[] values = data.get(id);
        return (values != null && values.length > 0) ? values[0] : null;
    }

    public Double getArea(long id) {
        Double[] values = data.get(id);
        return (values != null && values.length > 1) ? values[1] : null;
    }

}
