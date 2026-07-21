package com.kalyani.car_rental_backend.config;

import com.kalyani.car_rental_backend.car.entity.Car;
import com.kalyani.car_rental_backend.car.repository.CarRepository;
import com.kalyani.car_rental_backend.driver.entity.DriverProfile;
import com.kalyani.car_rental_backend.driver.repository.DriverProfileRepository;
import com.kalyani.car_rental_backend.user.entity.Role;
import com.kalyani.car_rental_backend.user.entity.User;
import com.kalyani.car_rental_backend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository users;
    private final CarRepository cars;
    private final PasswordEncoder encoder;
    private final DriverProfileRepository drivers;

    @Value("${admin.email}")
    private String email;

    @Value("${admin.password}")
    private String password;

    @Value("${admin.name}")
    private String name;

    public DataSeeder(
            UserRepository users,
            CarRepository cars,
            PasswordEncoder encoder,
            DriverProfileRepository drivers
    ) {
        this.users = users;
        this.cars = cars;
        this.encoder = encoder;
        this.drivers = drivers;
    }

    @Override
    public void run(String... args) {

        seedAdmin();
        seedDrivers();
        seedCars();
    }

    /*
     * Create the default admin account only when it does not exist.
     */
    private void seedAdmin() {

        if (!users.existsByEmailIgnoreCase(email)) {

            User admin = new User();

            admin.setFullName(name);
            admin.setEmail(email.toLowerCase());
            admin.setPassword(encoder.encode(password));
            admin.setRole(Role.ADMIN);
            admin.setProvider("LOCAL");
            admin.setStatus("ACTIVE");
            admin.setEmailVerified(true);

            users.save(admin);

            System.out.println("Default admin created successfully.");
        }
    }

    /*
     * Create two approved sample drivers for Phase 2 testing.
     * These accounts can log in immediately and update online status/location.
     */
    private void seedDrivers() {
        createOrUpdateDriver("Rahul Patil","rahul.driver@rentride.com","Driver@123","9876543211","MH12DL2026","1234","MH12DR1001","Maruti Suzuki Dzire",18.5204,73.8567);
        createOrUpdateDriver("Amit Jadhav","amit.driver@rentride.com","Driver@123","9876543212","MH14DL2026","5678","MH14DR1002","Hyundai Aura",18.5074,73.8077);
        createOrUpdateDriver("Suresh More","suresh.driver@rentride.com","Driver@123","9876543213","MH01DL2026","2468","MH01DR1003","Toyota Etios",19.0760,72.8777);
        createOrUpdateDriver("Nilesh Pawar","nilesh.driver@rentride.com","Driver@123","9876543214","MH15DL2026","1357","MH15DR1004","Maruti Suzuki Ertiga",19.9975,73.7898);
        createOrUpdateDriver("Vijay Mane","vijay.driver@rentride.com","Driver@123","9876543215","MH10DL2026","8642","MH10DR1005","Tata Tigor",16.8524,74.5815);
        createOrUpdateDriver("Rohan Shinde","rohan.driver@rentride.com","Driver@123","9876543216","MH09DL2026","9753","MH09DR1006","Honda Amaze",16.7050,74.2433);
        createOrUpdateDriver("Akash Deshmukh","akash.driver@rentride.com","Driver@123","9876543217","MH04DL2026","4321","MH04DR1007","Hyundai Xcent",19.2183,72.9781);
    }

    private void createOrUpdateDriver(
            String fullName,
            String driverEmail,
            String driverPassword,
            String phone,
            String licenseNumber,
            String aadhaarLast4,
            String vehicleNumber,
            String vehicleModel,
            double latitude,
            double longitude
    ) {
        User user = users.findByEmailIgnoreCase(driverEmail).orElseGet(User::new);
        boolean newUser = user.getId() == null;

        user.setFullName(fullName);
        user.setEmail(driverEmail.toLowerCase());
        user.setPhone(phone);
        user.setRole(Role.DRIVER);
        user.setProvider("LOCAL");
        user.setStatus("ACTIVE");
        user.setEmailVerified(true);
        if (newUser) {
            user.setPassword(encoder.encode(driverPassword));
        }
        user = users.save(user);

        DriverProfile profile = drivers.findByUserEmailIgnoreCase(driverEmail)
                .orElseGet(DriverProfile::new);
        profile.setUser(user);
        profile.setVerificationStatus("APPROVED");
        profile.setOnline(true);
        profile.setLicenseNumber(licenseNumber);
        profile.setAadhaarLast4(aadhaarLast4);
        profile.setVehicleNumber(vehicleNumber);
        profile.setVehicleModel(vehicleModel);
        profile.setCurrentLatitude(latitude);
        profile.setCurrentLongitude(longitude);
        drivers.save(profile);

        if (newUser) {
            System.out.println("Sample driver created: " + driverEmail);
        }
    }

    /*
     * Add or update the four demonstration cars.
     */
    private void seedCars() {

        /*
         * Maruti Suzuki Swift
         */
        addOrUpdateCar(
                "Swift",
                "Maruti Suzuki",
                "ZXI",
                2024,
                "White",
                "MH12AB1001",
                "Petrol",
                "Manual",
                5,
                1800,
                11000,
                42000,
                "Pune",
                18.5204,
                73.8567,
                "ICICI Lombard",
                "INS-MH12AB1001",
                "Bluetooth, Android Auto, Apple CarPlay, Reverse Camera, "
                        + "ABS, Dual Airbags, Power Steering, Central Locking, "
                        + "Power Windows, Air Conditioning",
                "Full to Full",
                300,
                200,
                5000,
                "https://res.cloudinary.com/ezqhrsyl/image/upload/"
                        + "f_auto,q_auto,w_1200/"
                        + "v1784271132/Maruti_Suzuki_Swift_white_snareb",
                4.7
        );

        /*
         * Hyundai i20
         */
        addOrUpdateCar(
                "i20",
                "Hyundai",
                "Asta",
                2024,
                "Blue",
                "MH12AB1002",
                "Petrol",
                "Automatic",
                5,
                2200,
                14000,
                52000,
                "Mumbai",
                19.0760,
                72.8777,
                "HDFC ERGO",
                "INS-MH12AB1002",
                "Touchscreen Infotainment, Bluetooth, Android Auto, "
                        + "Apple CarPlay, Reverse Camera, ABS, Six Airbags, "
                        + "Automatic Climate Control, Cruise Control, "
                        + "Push Button Start, Rear Parking Sensors",
                "Full to Full",
                300,
                250,
                6000,
                "https://res.cloudinary.com/ezqhrsyl/image/upload/"
                        + "f_auto,q_auto,w_1200/"
                        + "v1784271131/Hyundai_i20_blue_yu6odk",
                4.8
        );

        /*
         * Tata Nexon
         */
        addOrUpdateCar(
                "Nexon",
                "Tata",
                "XZ+",
                2024,
                "Grey",
                "MH12AB1003",
                "Diesel",
                "Manual",
                5,
                2600,
                16500,
                62000,
                "Pune",
                18.5204,
                73.8567,
                "Tata AIG",
                "INS-MH12AB1003",
                "Touchscreen Infotainment, Android Auto, Apple CarPlay, "
                        + "Reverse Camera, ABS, Dual Airbags, Electronic "
                        + "Stability Control, Hill Hold Assist, Cruise Control, "
                        + "Automatic Climate Control, Rear Parking Sensors",
                "Full to Full",
                300,
                300,
                7000,
                "https://res.cloudinary.com/ezqhrsyl/image/upload/"
                        + "f_auto,q_auto,w_1200/"
                        + "v1784271164/Tata_Nexon_grey_ktpxv4",
                4.9
        );

        /*
         * Mahindra XUV700
         */
        addOrUpdateCar(
                "XUV700",
                "Mahindra",
                "AX7",
                2024,
                "Black",
                "MH12AB1004",
                "Diesel",
                "Automatic",
                7,
                4200,
                26500,
                99000,
                "Bengaluru",
                12.9716,
                77.5946,
                "Bajaj Allianz",
                "INS-MH12AB1004",
                "Panoramic Sunroof, Touchscreen Infotainment, Android Auto, "
                        + "Apple CarPlay, Reverse Camera, 360 Degree Camera, "
                        + "ABS, Seven Airbags, ADAS, Cruise Control, "
                        + "Automatic Climate Control, Hill Hold Assist, "
                        + "Electronic Stability Control, Premium Sound System",
                "Full to Full",
                350,
                400,
                12000,
                "https://res.cloudinary.com/ezqhrsyl/image/upload/"
                        + "f_auto,q_auto,w_1200/"
                        + "v1784271131/Mahindra_XUV700_black_hyujvj",
                4.9
        );
    }

    /*
     * Adds a new car or updates an existing car having the same
     * registration number.
     */
    private void addOrUpdateCar(
            String name,
            String brand,
            String model,
            int year,
            String color,
            String registrationNumber,
            String fuelType,
            String transmission,
            int seatingCapacity,
            int pricePerDay,
            int pricePerWeek,
            int pricePerMonth,
            String city,
            double latitude,
            double longitude,
            String insuranceProvider,
            String insurancePolicyNumber,
            String features,
            String fuelPolicy,
            int mileageLimitPerDay,
            int lateFeePerHour,
            int securityDeposit,
            String imageUrl,
            double rating
    ) {

        Car car = cars.findAll()
                .stream()
                .filter(existingCar ->
                        existingCar.getRegistrationNumber() != null
                                && existingCar
                                .getRegistrationNumber()
                                .equalsIgnoreCase(registrationNumber)
                )
                .findFirst()
                .orElseGet(Car::new);

        boolean isNewCar = car.getId() == null;

        car.setName(name);
        car.setBrand(brand);
        car.setModel(model);
        car.setYear(year);
        car.setColor(color);
        car.setRegistrationNumber(registrationNumber);

        car.setFuelType(fuelType);
        car.setTransmission(transmission);
        car.setSeatingCapacity(seatingCapacity);

        car.setPricePerDay(BigDecimal.valueOf(pricePerDay));
        car.setPricePerWeek(BigDecimal.valueOf(pricePerWeek));
        car.setPricePerMonth(BigDecimal.valueOf(pricePerMonth));

        car.setCurrentLocationCity(city);
        car.setCurrentLocationLatitude(latitude);
        car.setCurrentLocationLongitude(longitude);

        car.setInsuranceProvider(insuranceProvider);
        car.setInsurancePolicyNumber(insurancePolicyNumber);

        car.setFeatures(features);
        car.setFuelPolicy(fuelPolicy);
        car.setMileageLimitPerDay(mileageLimitPerDay);

        car.setLateFeePerHour(
                BigDecimal.valueOf(lateFeePerHour)
        );

        car.setSecurityDeposit(
                BigDecimal.valueOf(securityDeposit)
        );

        car.setImageUrl(imageUrl);
        car.setIsAvailable(true);
        car.setAvailabilityStatus("AVAILABLE");
        car.setRating(rating);

        cars.save(car);

        if (isNewCar) {
            System.out.println(
                    brand + " " + name + " added successfully."
            );
        } else {
            System.out.println(
                    brand + " " + name + " updated successfully."
            );
        }
    }
}