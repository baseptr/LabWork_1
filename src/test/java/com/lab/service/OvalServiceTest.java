package com.lab.service;

import com.lab.entity.Oval;
import com.lab.entity.Point;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class OvalServiceTest {

    private OvalService service;
    private static final double DELTA = 1E-6; // Допуск для сравнения double

    @BeforeClass // Инициализируем сервис один раз для всех тестов класса
    public void setUp() {
        service = new OvalService();
    }

    // --- Тесты для calculateArea ---
    @Test(dataProvider = "areaTestData")
    public void testCalculateArea(Oval oval, double expectedArea) {
        double actualArea = service.calculateArea(oval);
        Assert.assertEquals(actualArea, expectedArea, DELTA, "Ошибка расчёта площади для овала: " + oval);
    }

    @DataProvider(name = "areaTestData")
    public Object[][] areaTestDataProvider() {
        // Овал с p1=(0,0), p2=(2,4) -> оси=2,4 -> полуоси a=1, b=2 -> Area = PI*1*2 = 2*PI
        Oval oval1 = new Oval(new Point(0, 0), new Point(2, 4));
        // Круг с p1=(0,0), p2=(2,2) -> оси=2,2 -> полуоси a=1, b=1 -> Area = PI*1*1 = PI
        Oval oval2 = new Oval(new Point(0, 0), new Point(2, 2));
        // Овал с p1=(-1,-1), p2=(1,1) -> оси=2,2 -> полуоси a=1, b=1 -> Area = PI
        Oval oval3 = new Oval(new Point(-1, -1), new Point(1, 1));
        // Овал с p1=(1,5), p2=(5,1) -> оси=4,4 -> полуоси a=2, b=2 -> Area = PI*2*2 = 4*PI
        Oval oval4 = new Oval(new Point(1, 5), new Point(5, 1));

        return new Object[][] {
                {oval1, 2.0 * Math.PI},
                {oval2, Math.PI},
                {oval3, Math.PI},
                {oval4, 4.0 * Math.PI}
        };
    }

    // --- Тесты для calculatePerimeter (используем приближенную формулу из сервиса) ---
    @Test(dataProvider = "perimeterTestData")
    public void testCalculatePerimeter(Oval oval, double expectedPerimeter) {
        double actualPerimeter = service.calculatePerimeter(oval);
        Assert.assertEquals(actualPerimeter, expectedPerimeter, DELTA, "Ошибка расчёта периметра для овала: " + oval);
    }

    @DataProvider(name = "perimeterTestData")
    public Object[][] perimeterTestDataProvider() {
        // Овал с p1=(0,0), p2=(2,4) -> a=1, b=2 -> P ≈ 2*PI*sqrt((1+4)/2) = 2*PI*sqrt(2.5)
        Oval oval1 = new Oval(new Point(0, 0), new Point(2, 4));
        double expectedP1 = 2.0 * Math.PI * Math.sqrt((1.0 + 2.0*2.0) / 2.0);
        // Круг с p1=(0,0), p2=(2,2) -> a=1, b=1 -> P ≈ 2*PI*sqrt((1+1)/2) = 2*PI*1 = 2*PI
        Oval oval2 = new Oval(new Point(0, 0), new Point(2, 2));
        double expectedP2 = 2.0 * Math.PI; // Точная формула для круга 2*PI*R

        return new Object[][] {
                {oval1, expectedP1},
                {oval2, expectedP2} // Для круга формула точна
        };
    }

    // --- Тесты для isCircle ---
    @Test(dataProvider = "circleTestData")
    public void testIsCircle(Oval oval, boolean expectedIsCircle) {
        boolean actualIsCircle = service.isCircle(oval);
        Assert.assertEquals(actualIsCircle, expectedIsCircle, "Ошибка проверки на окружность для овала: " + oval);
    }

    @DataProvider(name = "circleTestData")
    public Object[][] circleTestDataProvider() {
        Oval notCircle = new Oval(new Point(0, 0), new Point(2, 4)); // a=1, b=2
        Oval isCircle1 = new Oval(new Point(0, 0), new Point(2, 2)); // a=1, b=1
        Oval isCircle2 = new Oval(new Point(-5, -5), new Point(5, 5)); // a=5, b=5
        // Почти круг, но не совсем (проверка допуска)
        Oval almostCircle = new Oval(new Point(0,0), new Point(2.0, 2.00000001));

        return new Object[][] {
                {notCircle, false},
                {isCircle1, true},
                {isCircle2, true},
                {almostCircle, true} // Должен считаться кругом из-за DELTA/EPSILON
        };
    }
}