package com.apexretail.utilities;

/**
 * Provides temperature conversion utilities for scientific, engineering,
 * and business applications.
 *
 * <p>
 * All methods validate input temperatures against absolute zero and
 * throw {@link IllegalArgumentException} for invalid inputs.
 *
 * <p>
 * Example usage:
 * 
 * <pre>{@code
 * double f = TemperatureConverter.celsiusToFahrenheit(100.0);
 * double k = TemperatureConverter.celsiusToKelvin(0.0);
 * }</pre>
 *
 * @author David
 * @version 1.0.0
 */
public class TemperatureConverter {

    // Conversion constants (private to enforce encapsulation)
    private static final double C_TO_F_RATIO = 9.0 / 5.0;
    private static final double F_TO_C_RATIO = 5.0 / 9.0;
    private static final double F_OFFSET = 32.0;
    private static final double K_OFFSET = 273.15;

    // Absolute zero constants
    private static final double C_ABSOLUTE_ZERO = -273.15;
    private static final double F_ABSOLUTE_ZERO = -459.67;
    private static final double K_ABSOLUTE_ZERO = 0;

    // Helper for validation
    private static void validateAboveAbsoluteZero(double temp, double absoluteZero, String scale) {
        if (temp < absoluteZero) {
            throw new IllegalArgumentException(
                    String.format("Temperature %.2f°%s is below absolute zero", temp, scale));
        }
    }

    // ===== Conversion Methods =====

    public static double celsiusToFahrenheit(double celsius) {
        validateAboveAbsoluteZero(celsius, C_ABSOLUTE_ZERO, "C");
        return (celsius * C_TO_F_RATIO) + F_OFFSET;
    }

    public static double fahrenheitToCelsius(double fahrenheit) {
        validateAboveAbsoluteZero(fahrenheit, F_ABSOLUTE_ZERO, "F");
        return (fahrenheit - F_OFFSET) * F_TO_C_RATIO;
    }

    public static double celsiusToKelvin(double celsius) {
        validateAboveAbsoluteZero(celsius, C_ABSOLUTE_ZERO, "C");
        return celsius + K_OFFSET;
    }

    public static double kelvinToCelsius(double kelvin) {
        validateAboveAbsoluteZero(kelvin, K_ABSOLUTE_ZERO, "K");
        return kelvin - K_OFFSET;
    }

    public static double fahrenheitToKelvin(double fahrenheit) {
        validateAboveAbsoluteZero(fahrenheit, F_ABSOLUTE_ZERO, "F");
        return (fahrenheit - F_OFFSET) * F_TO_C_RATIO + K_OFFSET;
    }

    public static double kelvinToFahrenheit(double kelvin) {
        validateAboveAbsoluteZero(kelvin, K_ABSOLUTE_ZERO, "K");
        return (kelvin - K_OFFSET) * C_TO_F_RATIO + F_OFFSET;
    }
}
