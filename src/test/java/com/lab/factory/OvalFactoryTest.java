package com.lab.factory;

import com.lab.entity.Oval;
import com.lab.entity.Point;
import com.lab.exception.InvalidShapeException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class OvalFactoryTest {

    // --- Тесты на успешное создание ---
    @Test(dataProvider = "validOvalStrings")
    public void testCreateOvalSuccess(String data, Point expectedP1, Point expectedP2) throws InvalidShapeException {
        Oval actualOval = OvalFactory.createOval(data);
        Assert.assertNotNull(actualOval);
        // Сравниваем точки - record имеет корректный equals
        Assert.assertEquals(actualOval.getPointX(), expectedP1, "Несоответствие точки X для данных: " + data);
        Assert.assertEquals(actualOval.getPointY(), expectedP2, "Несоответствие точки Y для данных: " + data);
    }

    @DataProvider(name = "validOvalStrings")
    public Object[][] validOvalStringsProvider() {
        return new Object[][] {
                {"1.0,2.0;5.0,6.0", new Point(1.0, 2.0), new Point(5.0, 6.0)},
                {" 0,0 ; 10,10 ", new Point(0.0, 0.0), new Point(10.0, 10.0)}, // С пробелами
                {"-1.5,-2.5;3.5,0.5", new Point(-1.5, -2.5), new Point(3.5, 0.5)},
                {"100, 200 ; 300, 150", new Point(100.0, 200.0), new Point(300.0, 150.0)}
        };
    }

    // --- Тесты на ожидаемые исключения ---
    @Test(dataProvider = "invalidOvalStrings", expectedExceptions = InvalidShapeException.class)
    public void testCreateOvalFailure(String data) throws InvalidShapeException {
        OvalFactory.createOval(data); // Ожидаем, что этот вызов бросит исключение
    }

    @DataProvider(name = "invalidOvalStrings")
    public Object[][] invalidOvalStringsProvider() {
        return new Object[][] {
                // Ошибки формата строки
                {"1.0,2.0:5.0,6.0"}, // Неверный разделитель точек
                {"1.0,2.0;"},        // Нет второй точки
                {";5.0,6.0"},        // Нет первой точки
                {"1.0,2.0;5.0,6.0;7.0,8.0"}, // Слишком много точек
                {"1,2,3;4,5"},       // Неверное кол-во координат в P1
                {"1,2;4,5,6"},       // Неверное кол-во координат в P2
                // Ошибки парсинга чисел
                {"a,2.0;5.0,6.0"},
                {"1.0,b;5.0,6.0"},
                {"1.0, 2.0 ; 5..0, 6.0"},
                {",1.0;2.0,3.0"},      // Пустая координата
                // Ошибки геометрии (от валидатора)
                {"3.0,4.0;3.0,4.0"},   // Точки совпадают
                {"1.0,5.0;10.0,5.0"},  // Горизонтальная линия
                {"2.0,1.0;2.0,10.0"},  // Вертикальная линия
                // Пустые или null строки
                {""},
                {"   "},
                // {null} // DataProvider не любит null, это нужно тестить отдельно
        };
    }

    @Test(expectedExceptions = InvalidShapeException.class)
    public void testCreateOvalNullInput() throws InvalidShapeException {
        OvalFactory.createOval(null);
    }
}