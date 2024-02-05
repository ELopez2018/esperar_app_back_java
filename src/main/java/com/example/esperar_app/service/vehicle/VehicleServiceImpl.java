package com.example.esperar_app.service.vehicle;

import com.example.esperar_app.exception.ObjectNotFoundException;
import com.example.esperar_app.mapper.UserMapper;
import com.example.esperar_app.mapper.VehicleMapper;
import com.example.esperar_app.persistence.dto.inputs.vehicle.CreateVehicleDto;
import com.example.esperar_app.persistence.dto.inputs.vehicle.UpdateVehicleDto;
import com.example.esperar_app.persistence.dto.user.GetUserDto;
import com.example.esperar_app.persistence.dto.vehicle.GetVehicleDto;
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
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    private final UserMapper userMapper;

    @Autowired
    public VehicleServiceImpl(
            VehicleRepository vehicleRepository,
            VehicleMapper vehicleMapper,
            UserRepository userRepository,
            CompanyRepository companyRepository,
            UserMapper userMapper) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleMapper = vehicleMapper;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.userMapper = userMapper;
    }

    @Override
    public GetVehicleDto create(CreateVehicleDto createVehicleDto) {
        Vehicle vehicle = vehicleMapper.toEntity(createVehicleDto);

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

        Vehicle vehicleSaved = vehicleRepository.save(vehicle);

        return vehicleMapper.toGetVehicleDto(vehicleSaved);
    }

    @Override
    public Page<GetVehicleDto> findAll(Pageable pageable) {
        Page<Vehicle> vehiclesPage = vehicleRepository.findAll(pageable);

        return vehiclesPage.map(vehicleMapper::toGetVehicleDto);
    }

    @Override
    public GetVehicleDto findById(Long id) {
        Vehicle vehicleFound = vehicleRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Vehicle not found"));

        return vehicleMapper.toGetVehicle(vehicleFound);
    }

    @Override
    public GetVehicleDto update(Long id, UpdateVehicleDto updateVehicleDto) {
        Vehicle existingVehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Vehicle not found"));

        BeanUtils.copyProperties(updateVehicleDto, existingVehicle, getNullPropertyNames(updateVehicleDto));

        vehicleRepository.save(existingVehicle);

        return vehicleMapper.toGetVehicle(existingVehicle);
    }

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
        userRepository.save(driver);

        return vehicleSaved;
    }

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

        List<User> users = vehicleFound.getDrivers();

        return userMapper.toGetUserDtos(drivers);
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
