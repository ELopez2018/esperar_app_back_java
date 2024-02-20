package com.example.esperar_app.service.vehicle;

import com.example.esperar_app.exception.ObjectNotFoundException;
import com.example.esperar_app.mapper.UserMapper;
import com.example.esperar_app.mapper.VehicleMapper;
import com.example.esperar_app.persistence.dto.user.GetUserDto;
import com.example.esperar_app.persistence.dto.vehicle.CreateVehicleDto;
import com.example.esperar_app.persistence.dto.vehicle.GetVehicleDto;
import com.example.esperar_app.persistence.dto.vehicle.UpdateVehicleDto;
import com.example.esperar_app.persistence.entity.security.User;
import com.example.esperar_app.persistence.entity.vehicle.Vehicle;
import com.example.esperar_app.persistence.repository.VehicleRepository;
import com.example.esperar_app.persistence.repository.security.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.PropertyDescriptor;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final Pattern DATE_PATTERN = Pattern.compile("^\\d{2}-\\d{2}-\\d{4}$");

    @Autowired
    public VehicleServiceImpl(
            VehicleRepository vehicleRepository,
            VehicleMapper vehicleMapper,
            UserRepository userRepository,
            UserMapper userMapper) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleMapper = vehicleMapper;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    /**
     * Create a new vehicle
     * @param createVehicleDto Vehicle DTO to create a new vehicle
     * @return vehicle created
     */
    @Override
    public GetVehicleDto create(CreateVehicleDto createVehicleDto) {
        Vehicle vehicle = vehicleMapper.toEntity(createVehicleDto);

        validateUserExists();

        validateAndSetDate(createVehicleDto.getSoatExpirationDate(),
                "Invalid SOAT expiration date format",
                vehicle
        );

        validateAndSetDate(createVehicleDto.getTecnoMechanicalExpirationDate(),
                "Invalid technomecanical date format",
                vehicle
        );

        Vehicle vehicleSaved = vehicleRepository.save(vehicle);

        return vehicleMapper.toGetVehicleDto(vehicleSaved);
    }

    /**
     * Get all vehicles paginated
     * @param pageable Pageable object
     * @return Page of vehicles
     */
    @Override
    public Page<GetVehicleDto> findAll(Pageable pageable) {
        Page<Vehicle> vehiclesPage = vehicleRepository.findAll(pageable);

        return vehiclesPage.map(vehicleMapper::toGetVehicleDto);
    }

    /**
     * Get vehicle by identifier
     * @param id Vehicle identifier
     * @return Vehicle found
     */
    @Override
    public GetVehicleDto findById(Long id) {
        Vehicle vehicleFound = vehicleRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Vehicle not found"));

        return vehicleMapper.toGetVehicle(vehicleFound);
    }

    /**
     * Update vehicle by identifier
     * @param id Vehicle identifier
     * @param updateVehicleDto Vehicle DTO to update
     * @return Vehicle updated
     */
    @Override
    public GetVehicleDto update(Long id, UpdateVehicleDto updateVehicleDto) {
        Vehicle existingVehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Vehicle not found"));

        BeanUtils.copyProperties(updateVehicleDto, existingVehicle, getNullPropertyNames(updateVehicleDto));

        if(updateVehicleDto.getSoatExpirationDate() != null) {
            existingVehicle.setSoatExpirationDate(updateVehicleDto.getSoatExpirationDate());
        }

        if(updateVehicleDto.getTecnoMechanicalExpirationDate() != null) {
            existingVehicle.setTecnoMechanicalExpirationDate(updateVehicleDto.getTecnoMechanicalExpirationDate());
        }

        vehicleRepository.save(existingVehicle);

        return vehicleMapper.toGetVehicle(existingVehicle);
    }

    /**
     * Delete vehicle by identifier
     * @param id Vehicle identifier
     */
    @Override
    public void delete(Long id) {
        Vehicle vehicleFound = vehicleRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Vehicle not found"));

        try {
            vehicleRepository.delete(vehicleFound);
        } catch (Exception e) {
            throw new InternalError("Error deleting vehicle");
        }
    }

    /**
     * Assign driver to vehicle
     * @param id Vehicle identifier
     * @param driverId Driver identifier
     * @return Vehicle updated
     */
    @Override
    @Transactional
    public Vehicle assignDriver(Long id, Long driverId) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Vehicle not found"));

        User driver = userRepository.findById(driverId)
                .orElseThrow(() -> new ObjectNotFoundException("Driver not found"));

        for(User user : vehicle.getDrivers()) {
            if(user.getId().equals(driver.getId())) {
                throw new ObjectNotFoundException("Driver already assigned to this vehicle");
            }
        }

        vehicle.getDrivers().add(driver);
        Vehicle vehicleSaved = vehicleRepository.save(vehicle);

        driver.setVehicle(vehicleSaved);
        userRepository.save(driver);

        return vehicleSaved;
    }

    /**
     * Find vehicle drivers
     * @param id Vehicle identifier
     * @return Vehicle updated
     */
    @Override
    public List<GetUserDto> findVehicleDrivers(Long id) {
        List<User> drivers = userRepository.findVehicleDriversByVehicleId(id);

        Vehicle vehicleFound = vehicleRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Vehicle not found"));

        if(vehicleFound.getDrivers() == null) {
            System.out.println("Drivers is null");
        }

        if(vehicleFound.getDrivers().isEmpty()) {
            System.out.println("Drivers is empty");
        }

        return userMapper.toGetUserDtos(drivers);
    }

    /**
     * Get null properties of an object
     * @param source Object to get null properties
     * @return Array of null properties
     */
    private String[] getNullPropertyNames(Object source) {
        return getStrings(source);
    }

    /**
     * Get properties of an object
     * @param source Object to get properties
     * @return Array of properties
     */
    public static String[] getStrings(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * Validate date format
     * @param date Date to validate
     * @return True if date format is valid, false otherwise
     */
    private boolean isValidDateFormat(String date) {
        return DATE_PATTERN.matcher(date).matches();
    }

    /**
     * Validate date
     * @param date Date to validate
     * @return True if date is valid, false otherwise
     */
    private boolean isValidDate(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        try {
            simpleDateFormat.parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Validate user exists
     */
    private void validateUserExists() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        userRepository.findByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("Owner not found"));
    }

    /**
     * Validate and set date to create or update SOAT or technomecanical expiration date of a vehicle
     * @param date Date to validate
     * @param errorMessage Error message
     * @param vehicle Vehicle to set date
     */
    private void validateAndSetDate(String date, String errorMessage, Vehicle vehicle) {
        if (date != null) {
            if (isValidDateFormat(date) && isValidDate(date)) {
                setExpirationDate(date, errorMessage.contains("SOAT") ? "soat" : "technomecanical", vehicle);
            } else {
                throw new IllegalArgumentException(errorMessage + ", the correct format is dd-MM-yyyy");
            }
        }
    }

    /**
     * Set expiration date of a vehicle (SOAT or technomecanical)
     * @param date Date to set
     * @param type Type of expiration date (SOAT or technomecanical)
     * @param vehicle Vehicle to set date
     */
    private void setExpirationDate(String date, String type, Vehicle vehicle) {
        if ("soat".equals(type)) {
            vehicle.setSoatExpirationDate(date);
        } else if ("technomecanical".equals(type)) {
            vehicle.setTecnoMechanicalExpirationDate(date);
        }
    }

    /**
     * Cron job to validate expirations dates of vehicles every 5 seconds, if vehicle's expiration date is today
     * it will send a notification to the owner of the vehicle to notify that the expiration date is today and he
     * needs to renew it.
     */
    @Scheduled(fixedRate = 5000)
    public void checkExpiringDates() {
        List<Vehicle> vehicles = findVehiclesWithExpiringDates();

        System.out.println("Vehículos con expiración próxima: " + vehicles.size());

        for (Vehicle vehicle : vehicles) {
            System.out.println("Vehículo con expiración próxima: " + vehicle.getLicensePlate());
        }
    }

    /**
     * Find vehicles with expiring dates
     * @return List of vehicles with expiring dates
     */
    public List<Vehicle> findVehiclesWithExpiringDates() {
        Date currentDate = Date.from(LocalDate.now()
                .atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date sevenDaysLater = Date.from(LocalDate.now()
                .plusDays(7).atStartOfDay(ZoneId.systemDefault()).toInstant());

        return vehicleRepository.findBySoatExpirationDateBetweenOrTecnoMechanicalExpirationDateBetween(
                currentDate, sevenDaysLater,
                currentDate, sevenDaysLater
        );
    }
}
