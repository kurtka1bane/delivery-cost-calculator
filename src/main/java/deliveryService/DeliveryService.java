package deliveryService;

/**
 * Сервис расчёта стоимости доставки.
 */
public class DeliveryService {

    // Минимальная стоимость доставки
    static final int MIN_COST = 400;

    // Стоимость по расстоянию
    static final int DIST_OVER_30 = 300;
    static final int DIST_TO_30 = 200;
    static final int DIST_TO_10 = 100;
    static final int DIST_TO_2 = 50;

    // Стоимость по габаритам
    static final int SIZE_LARGE = 200;
    static final int SIZE_SMALL = 100;


    // Стоимость по хрупкости
    static final int FRAGILE_COST = 300;

    /**
     * Основной метод расчёта стоимости.
     * Возвращает -1 при ошибке
     */
    public static int calculateDeliveryCost(double distanceKm, PackageSize size, boolean isFragile, LoadLevel loadLevel) {
        try {
            if (distanceKm < 0) {
                throw new IllegalArgumentException("Расстояние не может быть отрицательным");
            }

            if (isFragile && distanceKm > 30) {
                throw new IllegalArgumentException("Хрупкий груз нельзя доставлять на расстояние более 30 км");
            }

            int cost = 0;

            // Расстояние
            if (distanceKm > 30) {
                cost += DIST_OVER_30;
            } else if (distanceKm > 10) {
                cost += DIST_TO_30;
            } else if (distanceKm > 2) {
                cost += DIST_TO_10;
            } else {
                cost += DIST_TO_2;
            }

            // Размер посылки
            cost += (size == PackageSize.LARGE) ? SIZE_LARGE : SIZE_SMALL ;

            // Добавление стоимости за хрупкость
            if (isFragile) {
                cost += FRAGILE_COST;
            }

            // Коэффициент загруженности
            double multiplier = switch (loadLevel) {
                case VERY_HIGH -> 1.6;
                case HIGH -> 1.4;
                case INCREASED -> 1.2;
                default -> 1.0;
            };

            cost = (int) Math.ceil(cost * multiplier);
            return Math.max(cost, MIN_COST);

        } catch (Exception e) {
            System.err.println("Ошибка при расчёте стоимости доставки: " + e.getMessage());
            return -1;
        }
    }
}
