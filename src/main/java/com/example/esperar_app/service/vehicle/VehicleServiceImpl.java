package com.example.esperar_app.service.vehicle;

import com.example.esperar_app.exception.ObjectNotFoundException;
import com.example.esperar_app.mapper.VehicleMapper;
import com.example.esperar_app.persistence.dto.inputs.vehicle.CreateVehicleDto;
import com.example.esperar_app.persistence.dto.inputs.vehicle.GetVehicle;
import com.example.esperar_app.persistence.dto.inputs.vehicle.UpdateVehicleDto;
import com.example.esperar_app.persistence.entity.vehicle.Vehicle;
import com.example.esperar_app.persistence.entity.company.Company;
import com.example.esperar_app.persistence.entity.security.User;
import com.example.esperar_app.persistence.repository.CompanyRepository;
import com.example.esperar_app.persistence.repository.VehicleRepository;
import com.example.esperar_app.persistence.repository.security.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    @Autowired
    public VehicleServiceImpl(
            VehicleRepository vehicleRepository,
            VehicleMapper vehicleMapper,
            UserRepository userRepository,
            CompanyRepository companyRepository) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleMapper = vehicleMapper;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public Vehicle create(CreateVehicleDto createVehicleDto) {
        Vehicle vehicle = vehicleMapper.toEntity(createVehicleDto);

        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();

            User owner = userRepository.findByUsername(username)
                    .orElseThrow(() -> new ObjectNotFoundException("Owner not found"));

            if(createVehicleDto.getCompanyId() != null) {
                Long companyId = createVehicleDto.getCompanyId();
                Company companyFound = companyRepository.findById(companyId)
                        .orElseThrow(() -> new ObjectNotFoundException("Company not found"));

                for(Company company : owner.getCompanies()) {
                    if(company.getId().equals(companyFound.getId())) {
                        vehicle.setCompany(company);
                        break;
                    }
                }
            }

            return vehicleRepository.save(vehicle);
        } catch (Exception e) {
            throw new ObjectNotFoundException
                    ("No tienes una empresa creada, para crear un vehículo primero crea una empresa");
        }
    }

    @Override
    public Page<GetVehicle> findAll(Pageable pageable) {
        Page<Vehicle> vehiclesPage = vehicleRepository.findAll(pageable);

        List<Vehicle> vehicles = vehiclesPage.getContent();
        List<GetVehicle> dtos = vehicles.isEmpty() ? Collections.emptyList() : vehicleMapper.toGetVehicles(vehicles);

        return new PageImpl<>(dtos, pageable, vehiclesPage.getTotalElements());
    }

    @Override
    public GetVehicle findById(Long id) {
        Vehicle vehicleFound = vehicleRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Vehicle not found"));

        GetVehicle getVehicle = vehicleMapper.toGetVehicle(vehicleFound);

        if(vehicleFound.getDrivers() != null) {
            List<Long> driversIds = new ArrayList<>();

            for(User driver : vehicleFound.getDrivers()) {
                driversIds.add(driver.getId());
            }

            getVehicle.setDriversIds(driversIds);
        }

        return getVehicle;
    }

    @Override
    public GetVehicle update(Long id, UpdateVehicleDto updateVehicleDto) {
        Vehicle existingVehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Vehicle not found"));

        BeanUtils.copyProperties(updateVehicleDto, existingVehicle, getNullPropertyNames(updateVehicleDto));

        vehicleRepository.save(existingVehicle);

        return vehicleMapper.toGetVehicle(existingVehicle);
    }

    @Override
    public void delete(Long id) {
        GetVehicle vehicleFound = findById(id);
        vehicleRepository.delete(vehicleMapper.toEntity(vehicleFound));
    }

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

        Company company = vehicleSaved.getCompany();
        driver.setVehicle(vehicleSaved);
        driver.setCompany(company);
        User userSaved = userRepository.save(driver);

        System.out.println("VEHICLE USER: " + userSaved.getVehicle().getId());

        return vehicleSaved;
    }

    @Override
    public List<User> findVehicleDrivers(Long id) {
        List<User> drivers = userRepository.findVehicleDriversByVehicleId(id);

        System.out.println("DRIVERS: " + drivers.size());

        Vehicle vehicleFound = vehicleRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Vehicle not found"));

        System.out.println("VEHICLE ID: " + vehicleFound.getId());

        if(vehicleFound.getDrivers() == null) {
            System.out.println("Drivers is null");
        }

        if(vehicleFound.getDrivers().isEmpty()) {
            System.out.println("Drivers is empty");
        }

        return vehicleFound.getDrivers();
    }

    private String[] getNullPropertyNames(Object source) {
        return getStrings(source);
    }

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

}
