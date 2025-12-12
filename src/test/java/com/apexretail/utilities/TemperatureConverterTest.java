package com.apexretail.utilities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for {@link TemperatureConverter}.
 * Tests include known conversions, symmetry, boundary conditions,
 * extreme values, and floating-point precision.
 */
class TemperatureConverterTest {

        private static final double DELTA = 0.0001;
        private static final double DELTA_LOW = 0.001;

        // ===== 1. KNOWN CONVERSIONS =====
        @ParameterizedTest(name = "{0}°C -> {1}°F -> {2}K")
        @CsvSource({
                        "0.0, 32.0, 273.15",
                        "100.0, 212.0, 373.15",
                        "-40.0, -40.0, 233.15",
                        "-273.15, -459.67, 0.0",
                        "37.0, 98.6, 310.15",
                        "20.0, 68.0, 293.15"
        })
        void testKnownConversions(double c, double f, double k) {
                assertEquals(f, TemperatureConverter.celsiusToFahrenheit(c), DELTA);
                assertEquals(c, TemperatureConverter.fahrenheitToCelsius(f), DELTA);

                assertEquals(k, TemperatureConverter.celsiusToKelvin(c), DELTA);
                assertEquals(c, TemperatureConverter.kelvinToCelsius(k), DELTA);

                assertEquals(k, TemperatureConverter.fahrenheitToKelvin(f), DELTA_LOW);
                assertEquals(f, TemperatureConverter.kelvinToFahrenheit(k), DELTA_LOW);
        }

        // ===== 2. SYMMETRY / ROUND-TRIP =====
        @ParameterizedTest
        @ValueSource(doubles = { -100.0, -10.5, 0.0, 25.7, 500.0, 10000.0 })
        void testConversionSymmetry(double celsius) {
                double f = TemperatureConverter.celsiusToFahrenheit(celsius);
                double k = TemperatureConverter.celsiusToKelvin(celsius);

                // C -> F -> C
                assertEquals(celsius, TemperatureConverter.fahrenheitToCelsius(f), DELTA);

                // C -> K -> C
                assertEquals(celsius, TemperatureConverter.kelvinToCelsius(k), DELTA);

                // F -> K -> F
                double fRoundTrip = TemperatureConverter.kelvinToFahrenheit(TemperatureConverter.fahrenheitToKelvin(f));
                assertEquals(f, fRoundTrip, DELTA_LOW);
        }

        // ===== 3. ABSOLUTE ZERO BOUNDARY =====
        @Test
        void testAbsoluteZeroBoundaries() {
                // Celsius
                assertDoesNotThrow(() -> TemperatureConverter.celsiusToFahrenheit(-273.15));
                assertThrows(IllegalArgumentException.class, () -> TemperatureConverter.celsiusToFahrenheit(-273.16));

                // Fahrenheit
                assertDoesNotThrow(() -> TemperatureConverter.fahrenheitToCelsius(-459.67));
                assertThrows(IllegalArgumentException.class, () -> TemperatureConverter.fahrenheitToCelsius(-459.68));

                // Kelvin
                assertDoesNotThrow(() -> TemperatureConverter.kelvinToCelsius(0.0));
                assertThrows(IllegalArgumentException.class, () -> TemperatureConverter.kelvinToCelsius(-0.0001));
        }

        // ===== 4. SPECIAL DOUBLE VALUES =====
        @Test
        void testNaNAndInfinity() {
                assertTrue(Double.isNaN(TemperatureConverter.celsiusToFahrenheit(Double.NaN)));
                assertEquals(Double.POSITIVE_INFINITY, TemperatureConverter.celsiusToKelvin(Double.POSITIVE_INFINITY));
                assertThrows(IllegalArgumentException.class,
                                () -> TemperatureConverter.celsiusToKelvin(Double.NEGATIVE_INFINITY));
        }

        // ===== 5. EXTREME BUT VALID VALUES =====
        @Test
        void testExtremeValues() {
                double largeC = Double.MAX_VALUE;
                assertDoesNotThrow(() -> {
                        double f = TemperatureConverter.celsiusToFahrenheit(largeC);
                        double k = TemperatureConverter.celsiusToKelvin(largeC);
                        assertTrue(Double.isFinite(f) || Double.isInfinite(f));
                        assertTrue(Double.isFinite(k) || Double.isInfinite(k));
                });

                double epsilonAboveZero = -273.15 + 1e-100;
                assertDoesNotThrow(() -> TemperatureConverter.celsiusToFahrenheit(epsilonAboveZero));
        }

        // ===== 6. PRECISION / ROUND-TRIP ACCUMULATION =====
        @Test
        void testPrecisionAfterMultipleConversions() {
                double initial = 42.123456789;
                double value = initial;
                for (int i = 0; i < 100; i++) {
                        value = TemperatureConverter
                                        .fahrenheitToCelsius(TemperatureConverter.celsiusToFahrenheit(value));
                }
                assertEquals(initial, value, DELTA_LOW * 10);
        }

        // ===== 7. CONSISTENCY OF CONVERSIONS =====
        @Test
        void testDirectVsIndirectConversions() {
                double f = 77.0;
                double direct = TemperatureConverter.fahrenheitToKelvin(f);
                double indirect = TemperatureConverter.celsiusToKelvin(TemperatureConverter.fahrenheitToCelsius(f));
                assertEquals(direct, indirect, DELTA, "Direct F->K should match indirect F->C->K");
        }

        // ===== 8. ZERO AND RANGE TESTS =====
        @Test
        void testZeroConversions() {
                assertEquals(32.0, TemperatureConverter.celsiusToFahrenheit(0.0), DELTA);
                assertEquals(273.15, TemperatureConverter.celsiusToKelvin(0.0), DELTA);
                assertEquals(-17.7778, TemperatureConverter.fahrenheitToCelsius(0.0), 0.0001);
        }

        @Test
        void testTemperatureRangeRoundTrip() {
                double[] temps = { -100.0, -50.0, -10.0, 0.0, 10.0, 50.0, 100.0 };
                for (double t : temps) {
                        double f = TemperatureConverter.celsiusToFahrenheit(t);
                        assertEquals(t, TemperatureConverter.fahrenheitToCelsius(f), DELTA);
                }
        }
}
