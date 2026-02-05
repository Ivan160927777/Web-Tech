package org.example;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
         final String url = "jdbc:postgresql://localhost:5432/1";
         final String user = "postgres";
         final String password = "postgres";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
        print_all(connection);
        System.out.println(printPerevoz(1,connection));
        //print_all(connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static String printPerevoz(int perevozId, Connection connection) {
        AutoDAO autoDAO = new AutoDAO(connection);
        DriverDAO driverDAO = new DriverDAO(connection);
        SkladDAO skladDAO = new SkladDAO(connection);
        GruzDAO gruzDAO = new GruzDAO(connection);
        PerevozDAO perevozDAO = new PerevozDAO(connection);
        try {
            Perevoz p = perevozDAO.getPerevozById(perevozId);
            if (p == null) {
                System.out.println("Перевозка с ID " + perevozId + " не найдена");
                return "";
            }

            Driver driver = driverDAO.getDriverById(p.getDriverId());
            Auto auto = autoDAO.getAutoById(p.getAutoId());
            Gruz gruz = gruzDAO.getGruzById(p.getGruzId());
            Sklad skladFrom = skladDAO.getSkladById(gruz.getSkladId()); // склад, откуда груз
            Sklad skladTo = skladDAO.getSkladById(p.getSkladId());      // склад назначения

            String output = String.format(
                    "Документ (%d). Водитель (%d.%s) на автомобиле (%d.%s) " +
                            "перевез груз (%s) со склада (%d.%s) на склад (%d.%s) %s.",
                    p.getId(),
                    driver.getId(), driver.getName(),
                    auto.getId(), auto.getModel(),
                    gruz.getName(),
                    skladFrom.getId(), skladFrom.getName(),
                    skladTo.getId(), skladTo.getName(),
                    p.getDate()
            );

            return output;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }
    public static void work(Connection connection) throws SQLException{
        // Создаем DAO
        AutoDAO autoDAO = new AutoDAO(connection);
        DriverDAO driverDAO = new DriverDAO(connection);
        SkladDAO skladDAO = new SkladDAO(connection);
        GruzDAO gruzDAO = new GruzDAO(connection);
        PerevozDAO perevozDAO = new PerevozDAO(connection);

        // ================== AUTO ==================
        System.out.println("=== AUTO ===");
        Auto auto = new Auto();
        auto.setModel("Toyota");
        auto.setGosNumber("AA1111-1");
        autoDAO.addAuto(auto);
        System.out.println("Inserted: " + auto);

        // Обновляем авто
        List<Auto> autos = autoDAO.getAllAutos();
        Auto firstAuto = autos.get(0);
        firstAuto.setModel("Updated Model");
        firstAuto.setGosNumber("ZZ9999-9");
        autoDAO.updateAuto(firstAuto);
        System.out.println("Updated: " + firstAuto);

        // Удаляем авто (второй элемент если есть)
        if (autos.size() > 1) {
            int idToDelete = autos.get(1).getId();
            autoDAO.deleteAuto(idToDelete);
            System.out.println("Deleted auto ID: " + idToDelete);
        }

        // ================== DRIVER ==================
        System.out.println("\n=== DRIVER ===");
        Driver driver = new Driver();
        driver.setName("Иван Иванов");
        driverDAO.addDriver(driver);
        System.out.println("Inserted: " + driver);

        List<Driver> drivers = driverDAO.getAllDrivers();
        Driver firstDriver = drivers.get(0);
        firstDriver.setName("Updated Driver");
        driverDAO.updateDriver(firstDriver);
        System.out.println("Updated: " + firstDriver);

        if (drivers.size() > 1) {
            int idToDelete = drivers.get(1).getId();
            driverDAO.deleteDriver(idToDelete);
            System.out.println("Deleted driver ID: " + idToDelete);
        }

        // ================== SKLAD ==================
        System.out.println("\n=== SKLAD ===");
        Sklad sklad = new Sklad();
        sklad.setName("Склад Тестовый");
        sklad.setAddress("г. Минск, ул. Тестовая, 1");
        skladDAO.addSklad(sklad);
        System.out.println("Inserted: " + sklad);

        List<Sklad> sklads = skladDAO.getAllSklads();
        Sklad firstSklad = sklads.get(0);
        firstSklad.setName("Updated Sklad");
        firstSklad.setAddress("г. Минск, ул. Новая, 2");
        skladDAO.updateSklad(firstSklad);
        System.out.println("Updated: " + firstSklad);

        if (sklads.size() > 1) {
            int idToDelete = sklads.get(1).getId();
            skladDAO.deleteSklad(idToDelete);
            System.out.println("Deleted sklad ID: " + idToDelete);
        }

        // ================== GRUZ ==================
        System.out.println("\n=== GRUZ ===");
        Gruz gruz = new Gruz();
        gruz.setName("Тестовый груз");
        gruz.setSkladId(firstSklad.getId());
        gruzDAO.addGruz(gruz);
        System.out.println("Inserted: " + gruz);

        List<Gruz> gruzList = gruzDAO.getAllGruz();
        Gruz firstGruz = gruzList.get(0);
        firstGruz.setName("Updated Gruz");
        firstGruz.setSkladId(firstSklad.getId());
        gruzDAO.updateGruz(firstGruz);
        System.out.println("Updated: " + firstGruz);

        if (gruzList.size() > 1) {
            int idToDelete = gruzList.get(1).getId();
            gruzDAO.deleteGruz(idToDelete);
            System.out.println("Deleted gruz ID: " + idToDelete);
        }

        // ================== PEREVOZ ==================
        System.out.println("\n=== PEREVOZ ===");
        Perevoz perevoz = new Perevoz();
        perevoz.setAutoId(firstAuto.getId());
        perevoz.setDriverId(firstDriver.getId());
        perevoz.setSkladId(firstSklad.getId());
        perevoz.setGruzId(firstGruz.getId());
        perevoz.setDate(Date.valueOf("2026-02-05"));
        perevozDAO.addPerevoz(perevoz);
        System.out.println("Inserted: " + perevoz);

        List<Perevoz> perevozList = perevozDAO.getAllPerevoz();
        Perevoz firstPerevoz = perevozList.get(0);
        firstPerevoz.setDate(Date.valueOf("2026-02-06"));
        perevozDAO.updatePerevoz(firstPerevoz);
        System.out.println("Updated: " + firstPerevoz);

        if (perevozList.size() > 1) {
            int idToDelete = perevozList.get(1).getId();
            perevozDAO.deletePerevoz(idToDelete);
            System.out.println("Deleted perevoz ID: " + idToDelete);
        }

    }
    public static void print_all(Connection connection) throws SQLException {
        // Создаем DAO
        AutoDAO autoDAO = new AutoDAO(connection);
        DriverDAO driverDAO = new DriverDAO(connection);
        SkladDAO skladDAO = new SkladDAO(connection);
        GruzDAO gruzDAO = new GruzDAO(connection);
        PerevozDAO perevozDAO = new PerevozDAO(connection);

        //Вывод всех авто
        System.out.println("=== Autos ===");
        List<Auto> autos = autoDAO.getAllAutos();
        for (Auto a : autos) {
            System.out.println(a);
        }

        // Вывод всех водителей
        System.out.println("\n=== Drivers ===");
        List<Driver> drivers = driverDAO.getAllDrivers();
        for (Driver d : drivers) {
            System.out.println(d);
        }

        // Вывод всех складов
        System.out.println("\n=== Sklads ===");
        List<Sklad> sklads = skladDAO.getAllSklads();
        for (Sklad s : sklads) {
            System.out.println(s);
        }

        //  Вывод всех грузов
        System.out.println("\n=== Gruz ===");
        List<Gruz> gruzList = gruzDAO.getAllGruz();
        for (Gruz g : gruzList) {
            System.out.println(g);
        }

        //  Вывод всех перевозок
        System.out.println("\n=== Perevoz ===");
        List<Perevoz> perevozList = perevozDAO.getAllPerevoz();
        for (Perevoz p : perevozList) {
            System.out.println(p);
        }
    }
}
