package lt.esdc.shapes.validator;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class OvalDataValidatorTest {

    private OvalDataValidator validator;

    @BeforeMethod
    public void setUp() {
        validator = new OvalDataValidator();
    }

    @DataProvider(name = "validOvalStrings")
    public Object[][] validOvalStringsProvider() {
        return new Object[][]{
                {"10.0;20.0;30.5;40.5"},
                {"-1.0;-2.0;-3.0;-4.0"},
                {"0;0;1;1"},
                {"100;200.75;300.1;400.12345"}
        };
    }

    @DataProvider(name = "invalidOvalStrings")
    public Object[][] invalidOvalStringsProvider() {
        return new Object[][]{
                {"1.0,2.0:5.0,6.0"},      // Invalid separator : and ,
                {"1.0;2.0;"},             // Missing two numbers
                {";5.0;6.0"},             // Missing two numbers at start
                {"1.0;2.0;5.0;6.0;7.0"},  // Too many numbers
                {"1;2;3"},                // Too few numbers
                {"a;2.0;5.0;6.0"},        // Non-numeric first part
                {"1.0;b;5.0;6.0"},        // Non-numeric second part
                {"1.0; 2.0 ; 5..0; 6.0"}, // Invalid number format (5..0)
                {",1.0;2.0;3.0"},         // Invalid char at start of number (comma)
                {"3.0;4.0;3.0"},          // Too few numbers
                {""},                     // Empty string
                {"   "},                  // Blank string
                {null}                    // Null string
        };
    }

    @Test(dataProvider = "validOvalStrings")
    public void testIsValid_validData_shouldReturnTrue(String validData) {
        boolean result = validator.isValid(validData);
        assertTrue(result);
    }

    @Test(dataProvider = "invalidOvalStrings")
    public void testIsValid_invalidData_shouldReturnFalse(String invalidData) {
        boolean result = validator.isValid(invalidData);
        assertFalse(result);
    }
}