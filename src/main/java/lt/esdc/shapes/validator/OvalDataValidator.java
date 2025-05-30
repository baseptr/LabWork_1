package lt.esdc.shapes.validator;

import java.util.regex.Pattern;

public class OvalDataValidator implements ShapeDataValidator {


    private static final String OVAL_DATA_REGEX =
            "^-?\\d+(\\.\\d+)?;-?\\d+(\\.\\d+)?;-?\\d+(\\.\\d+)?;-?\\d+(\\.\\d+)?$";
    private static final Pattern OVAL_DATA_PATTERN = Pattern.compile(OVAL_DATA_REGEX);

    @Override
    public boolean isValid(String dataLine) {
        if (dataLine == null || dataLine.trim().isEmpty()) {
            return false;
        }
        return OVAL_DATA_PATTERN.matcher(dataLine.trim()).matches();
    }
}