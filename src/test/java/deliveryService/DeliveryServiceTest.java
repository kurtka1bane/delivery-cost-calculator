package deliveryService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Набор автотестов для проверки логики расчета стоимости доставки в DeliveryService.

 В тестах проверяются следующие кейсы:
 * - Расчет базовой стоимости в зависимости от расстояния и размера посылки
 * - Доплата за хрупкость
 * - Применение коэффициентов при различной загрузке (NORMAL, HIGH, VERY_HIGH)
 * - Округление итоговой стоимости после умножения
 * - Соблюдение минимальной стоимости доставки (не ниже 400)
 * - Обработка некорректных входных данных (например, отрицательное расстояние, хрупкий груз дальше 30 км)
 * - Проверка расчетов на граничных значениях (0, 2, 10, 30 км)
 * - Проверка комбинаций параметров (размер, хрупкость, расстояние, загрузка)
 */

class DeliveryServiceTest {
    /**
     * Большая посылка, 5 км, без хрупкости, нормальная загруженность.
     * 100 (дистанция) + 200 (размер) = 300 → применяется минимальная стоимость 400
     */
    @Test
    void shouldCalculateBaseCostForLargePackage() {
        int cost = DeliveryService.calculateDeliveryCost(5, PackageSize.LARGE, false, LoadLevel.NORMAL);
        assertEquals(400, cost);
    }
    /**
     * Добавление стоимости за хрупкость.
     * 100 + 100 + 300 = 500 → выше минимальной
     */
    @Test
    void shouldIncludeFragileCost() {
        int cost = DeliveryService.calculateDeliveryCost(5, PackageSize.SMALL, true, LoadLevel.NORMAL);
        assertEquals(500, cost);
    }

    /**
     * Округление вверх после умножения на коэффициент.
     * (200 + 100) * 1.4 = 420
     */
    @Test
    void shouldRoundUpAfterMultiplication() {
        int cost = DeliveryService.calculateDeliveryCost(11, PackageSize.SMALL, false, LoadLevel.HIGH);
        assertEquals(420, cost);
    }

    /**
     * Расстояние 0 км, сумма меньше минимальной → 400
     */
    @Test
    void shouldCalculateMinCost() {
        int cost = DeliveryService.calculateDeliveryCost(0, PackageSize.SMALL, false, LoadLevel.NORMAL);
        assertEquals(400, cost);
    }

    /**
     * Расстояние больше 30 км, высокая загруженность.
     * (300 + 100) * 1.6 = 640
     */
    @Test
    void shouldCalculateCostWithVeryHighLoad() {
        int cost = DeliveryService.calculateDeliveryCost(31, PackageSize.SMALL, false, LoadLevel.VERY_HIGH);
        assertEquals(640, cost);
    }

    /**
     * Отрицательное расстояние → ошибка
     */

    @Test
    void shouldReturnErrorForNegativeDistance() {
        int cost = DeliveryService.calculateDeliveryCost(-1, PackageSize.SMALL, false, LoadLevel.NORMAL);
        assertEquals(-1, cost);
    }

    /**
     * Хрупкая посылка на 31 км → ошибка
     */
    @Test
    void shouldReturnErrorForFragileOverMaxDistance() {
        int cost = DeliveryService.calculateDeliveryCost(31, PackageSize.SMALL, true, LoadLevel.NORMAL);
        assertEquals(-1, cost);
    }

    /**
     * Граничные расстояния: 0, 2, 10, 30 км.
     * Все суммы выше 400, что бы увидеть реально ли работает надбавочная стоимость
     */
    @ParameterizedTest
    @CsvSource({
            // distance ≤ 30 → fragile = true (гарантирует >400)
            "0.0, 550",
            "2.0, 550",
            "2.01, 600",
            "10.0, 600",
            "10.01, 700",
            "15.0, 700",
            "30.0, 700",


            "30.01, 700",
            "40.0, 700"
    })
    void shouldAlwaysBeAbove400(double distance, int expectedCost) {
        int result;

        if (distance > 30) {
            // Для >30 км нельзя fragile, используем HIGH загруженность
            result = DeliveryService.calculateDeliveryCost(distance, PackageSize.LARGE, false, LoadLevel.HIGH);
        } else {
            // Для ≤30 км используем fragile
            result = DeliveryService.calculateDeliveryCost(distance, PackageSize.LARGE, true, LoadLevel.NORMAL);
        }

        assertEquals(expectedCost, result, "Неверная стоимость при distance = " + distance);
        assertTrue(result > 400, "Стоимость должна быть больше 400");
    }

    /**
     * Комбинация хрупкость + загрузка HIGH + 5 км.
     * (100 + 100 + 300) * 1.4 = 700
     */
    @Test
    void shouldCalculateCostWithCombinations() {
        int cost = DeliveryService.calculateDeliveryCost(5, PackageSize.SMALL, true, LoadLevel.HIGH);
        assertEquals(700, cost);
    }

    /**
     * LoadLevel.INCREASED
     * (300 + 100) * 1.2 = 480
     */
    @Test
    void shouldApplyIncreasedLoadMultiplier() {
        int cost = DeliveryService.calculateDeliveryCost(40, PackageSize.SMALL, false, LoadLevel.INCREASED);
        assertEquals(480, cost);
    }
}
