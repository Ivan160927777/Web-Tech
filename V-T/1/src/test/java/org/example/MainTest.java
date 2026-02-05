package org.example;

import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MainTest {

    private static Connection connection;
    private static AutoDAO autoDAO;
    private static DriverDAO driverDAO;
    private static SkladDAO skladDAO;
    private static GruzDAO gruzDAO;
    private static PerevozDAO perevozDAO;

    @BeforeAll
    static void setup() throws SQLException {
        final String url = "jdbc:postgresql://localhost:5432/1";
        final String user = "postgres";
        final String password = "postgres";

        connection = DriverManager.getConnection(url, user, password);

        autoDAO = new AutoDAO(connection);
        driverDAO = new DriverDAO(connection);
        skladDAO = new SkladDAO(connection);
        gruzDAO = new GruzDAO(connection);
        perevozDAO = new PerevozDAO(connection);

        // Добавляем тестовые данные (если они ещё не существуют)
        if (autoDAO.getAllAutos().isEmpty()) {
            autoDAO.addAuto(new Auto(0, "МАЗ", "АВ1234-1"));
        }

        if (driverDAO.getAllDrivers().isEmpty()) {
            driverDAO.addDriver(new Driver(0, "Иванов Сергей"));
        }

        if (skladDAO.getAllSklads().isEmpty()) {
            skladDAO.addSklad(new Sklad(0, "Склад Северный", "ул. Ленина, 10"));
        }

        if (gruzDAO.getAllGruz().isEmpty()) {
            gruzDAO.addGruz(new Gruz(0, "Строительные материалы", skladDAO.getAllSklads().get(0).getId()));
        }

        if (perevozDAO.getAllPerevoz().isEmpty()) {
            Perevoz p = new Perevoz(0,
                    autoDAO.getAllAutos().get(0).getId(),
                    gruzDAO.getAllGruz().get(0).getId(),
                    skladDAO.getAllSklads().get(0).getId(),
                    driverDAO.getAllDrivers().get(0).getId(),
                    Date.valueOf("2025-02-05"));
            perevozDAO.addPerevoz(p);
        }
    }

    @AfterAll
    static void teardown() throws SQLException {
        connection.close();
    }

    // ================== Проверка подключения ==================
    @Test
    @Order(1)
    @DisplayName("Проверка подключения к PostgreSQL")
    void testConnection() throws SQLException {
        assertNotNull(connection, "Connection не должен быть null");
        assertFalse(connection.isClosed(), "Connection должен быть открыт");
    }

    // ================== CRUD тесты DAO ==================
    @Test
    @Order(2)
    @DisplayName("Тест AutoDAO")
    void testAutoDAO() throws SQLException {
        List<Auto> autos = autoDAO.getAllAutos();
        assertFalse(autos.isEmpty(), "Должен быть хотя бы один автомобиль");

        Auto auto = autos.get(0);
        assertEquals("МАЗ", auto.getModel());
        // Update
        auto.setModel("Updated MAZ");
        autoDAO.updateAuto(auto);
        Auto updated = autoDAO.getAutoById(auto.getId());
        assertEquals("Updated MAZ", updated.getModel());
        auto.setModel("МАЗ");
        autoDAO.updateAuto(auto);

    }

    @Test
    @Order(3)
    @DisplayName("Тест DriverDAO")
    void testDriverDAO() throws SQLException {
        List<Driver> drivers = driverDAO.getAllDrivers();
        assertFalse(drivers.isEmpty());

        Driver driver = drivers.get(0);
        assertEquals("Иванов Сергей", driver.getName());

        // Update
        driver.setName("Updated Driver");
        driverDAO.updateDriver(driver);
        Driver updated = driverDAO.getDriverById(driver.getId());
        assertEquals("Updated Driver", updated.getName());
        driver.setName("Иванов Сергей");
        driverDAO.updateDriver(driver);
    }

    // ================== Тест Main.printPerevoz ==================
    @Test
    @Order(4)
    @DisplayName("Тест Main.printPerevoz")
    void testPrintPerevoz() {
        String info = Main.printPerevoz(1, connection);
        assertTrue(info.contains("Документ (1)"));
        assertTrue(info.contains("1.Иванов Сергей"));
        assertTrue(info.contains("1.МАЗ"));
        assertTrue(info.contains("1.Склад Северный"));
        assertTrue(info.contains("2025-02-01"));
    }

    // ================== Тест Main.print_all ==================
    @Test
    @Order(5)
    @DisplayName("Тест Main.print_all")
    void testPrintAll() throws SQLException {
        // Метод выводит в консоль, проверяем, что не падает
        assertDoesNotThrow(() -> Main.print_all(connection));
    }
}
