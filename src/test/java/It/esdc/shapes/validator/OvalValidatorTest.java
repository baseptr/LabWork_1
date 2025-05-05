package It.esdc.shapes.validator;

import It.esdc.shapes.entity.Oval;
import It.esdc.shapes.entity.Point;
import It.esdc.shapes.exception.InvalidShapeException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class OvalValidatorTest {

    // --- Тест на валидный овал (не должен бросать исключение) ---
    @Test(dataProvider = "validOvals")
    public void testValidateValidOval(Oval oval) {
        // Используем try-catch для проверки отсутствия исключения,
        // т.к. TestNG @Test без expectedExceptions ожидает успешного выполнения
        try {
            OvalValidator.validate(oval);
            // Если дошли сюда, исключения не было - тест пройден
            Assert.assertTrue(true); // Для наглядности
        } catch (InvalidShapeException e) {
            Assert.fail("Валидация не пройдена для корректного овала: " + oval, e);
        }
    }

    @DataProvider(name = "validOvals")
    public Object[][] validOvalsProvider() {
        return new Object[][] {
                {new Oval(new Point(0, 0), new Point(2, 4))},
                {new Oval(new Point(-1, -1), new Point(1, 1))},
                {new Oval(new Point(10, 20), new Point(11, 21))}
        };
    }


    // --- Тесты на невалидный овал (должен бросать исключение) ---
    @Test(dataProvider = "invalidOvals", expectedExceptions = InvalidShapeException.class)
    public void testValidateInvalidOval(Oval oval) throws InvalidShapeException {
        OvalValidator.validate(oval); // Ожидаем исключение
    }

    @DataProvider(name = "invalidOvals")
    public Object[][] invalidOvalsProvider() {
        return new Object[][] {
                // Точки совпадают
                {new Oval(new Point(3.0, 4.0), new Point(3.0, 4.0))},
                // Горизонтальная линия
                {new Oval(new Point(1.0, 5.0), new Point(10.0, 5.0))},
                // Вертикальная линия
                {new Oval(new Point(2.0, 1.0), new Point(2.0, 10.0))},
                // Овалы с null точками (если конструктор Oval их пропускает)
                // {new Oval(null, new Point(1,1))}, // Лучше не создавать такие объекты
                // {new Oval(new Point(1,1), null)},
        };
    }

    @Test(expectedExceptions = InvalidShapeException.class)
    public void testValidateNullOval() throws InvalidShapeException {
        OvalValidator.validate(null);
    }
}