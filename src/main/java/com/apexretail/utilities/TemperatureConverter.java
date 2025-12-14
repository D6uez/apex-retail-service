package com.apexretail.utilities;

/**
 * Temperature conversion utilities for Celsius, Fahrenheit, and Kelvin scales.
 * All methods validate against absolute zero and throw IllegalArgumentException
 * for invalid inputs.
 *
 * <p>
 * Example:
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

    // Conversion constants
    private static final double C_TO_F_RATIO = 9.0 / 5.0;
    private static final double F_TO_C_RATIO = 5.0 / 9.0;
    private static final double F_OFFSET = 32.0;
    private static final double K_OFFSET = 273.15;

    // Absolute zero constants
    private static final double C_ABSOLUTE_ZERO = -273.15;
    private static final double F_ABSOLUTE_ZERO = -459.67;
    private static final double K_ABSOLUTE_ZERO = 0;

    /**
     * Validates temperature is above absolute zero.
     * 
     * @param temp         temperature to validate
     * @param absoluteZero minimum allowed temperature for scale
     * @param scale        temperature scale ("C", "F", or "K")
     * @throws IllegalArgumentException if temperature is below absolute zero
     */
    private static void validateAboveAbsoluteZero(double temp, double absoluteZero, String scale) {
        if (temp < absoluteZero) {
            throw new IllegalArgumentException(
                    String.format("Temperature %.2f°%s is below absolute zero", temp, scale));
        }
    }

    /**
     * Converts Celsius to Fahrenheit.
     * 
     * @param celsius temperature in Celsius
     * @return temperature in Fahrenheit
     * @throws IllegalArgumentException if celsius < -273.15
     */
    public static double celsiusToFahrenheit(double celsius) {
        validateAboveAbsoluteZero(celsius, C_ABSOLUTE_ZERO, "C");
        return (celsius * C_TO_F_RATIO) + F_OFFSET;
    }

    /**
     * Converts Fahrenheit to Celsius.
     * 
     * @param fahrenheit temperature in Fahrenheit
     * @return temperature in Celsius
     * @throws IllegalArgumentException if fahrenheit < -459.67
     */
    public static double fahrenheitToCelsius(double fahrenheit) {
        validateAboveAbsoluteZero(fahrenheit, F_ABSOLUTE_ZERO, "F");
        return (fahrenheit - F_OFFSET) * F_TO_C_RATIO;
    }

    /**
     * Converts Celsius to Kelvin.
     * 
     * @param celsius temperature in Celsius
     * @return temperature in Kelvin
     * @throws IllegalArgumentException if celsius < -273.15
     */
    public static double celsiusToKelvin(double celsius) {
        validateAboveAbsoluteZero(celsius, C_ABSOLUTE_ZERO, "C");
        return celsius + K_OFFSET;
    }

    /**
     * Converts Kelvin to Celsius.
     * 
     * @param kelvin temperature in Kelvin
     * @return temperature in Celsius
     * @throws IllegalArgumentException if kelvin < 0
     */
    public static double kelvinToCelsius(double kelvin) {
        validateAboveAbsoluteZero(kelvin, K_ABSOLUTE_ZERO, "K");
        return kelvin - K_OFFSET;
    }

    /**
     * Converts Fahrenheit to Kelvin.
     * 
     * @param fahrenheit temperature in Fahrenheit
     * @return temperature in Kelvin
     * @throws IllegalArgumentException if fahrenheit < -459.67
     */
    public static double fahrenheitToKelvin(double fahrenheit) {
        validateAboveAbsoluteZero(fahrenheit, F_ABSOLUTE_ZERO, "F");
        return (fahrenheit - F_OFFSET) * F_TO_C_RATIO + K_OFFSET;
    }

    /**
     * Converts Kelvin to Fahrenheit.
     * 
     * @param kelvin temperature in Kelvin
     * @return temperature in Fahrenheit
     * @throws IllegalArgumentException if kelvin < 0
     */
    public static double kelvinToFahrenheit(double kelvin) {
        validateAboveAbsoluteZero(kelvin, K_ABSOLUTE_ZERO, "K");
        return (kelvin - K_OFFSET) * C_TO_F_RATIO + F_OFFSET;
    }
}