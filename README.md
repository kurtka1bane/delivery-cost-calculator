## 🚚 Delivery Cost Calculator

Проект включает сервис расчёта стоимости доставки с реализованной бизнес-логикой и набор автоматизированных юнит-тестов для её проверки.

## 📌 Технологии
- Java 17
- Maven
- JUnit 5

## 🚀 Запуск тестов
```bash```
mvn test

## 📦 Отчёты

Результаты тестов выводятся в консоль.

## 📁 Структура проекта
  delivery-cost-calculator/
├── src/
│   ├── main/
│   │   └── java/deliveryService/
│   └── test/
│       └── java/deliveryService/
├── pom.xml
└── README.md

## ✅ Покрытие тестами

- Базовый расчёт стоимости
- Ограничения для хрупких грузов
- Коэффициенты загрузки
- Обработка ошибок
- Граничные значения
- Комбинации параметров

## 📝 Пример использования

```java
// Расчёт стоимости:
int cost = DeliveryService.calculateDeliveryCost(
    15.0,
    PackageSize.LARGE,
    true,
    LoadLevel.HIGH
);
