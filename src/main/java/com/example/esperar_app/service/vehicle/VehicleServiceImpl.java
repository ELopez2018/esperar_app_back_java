package com.example.esperar_app.service.vehicle;

import com.example.esperar_app.exception.ObjectNotFoundException;
import com.example.esperar_app.mapper.VehicleMapper;
import com.example.esperar_app.persistence.dto.inputs.vehicle.CreateVehicleDto;
import com.example.esperar_app.persistence.dto.inputs.vehicle.GetVehicle;
import com.example.esperar_app.persistence.dto.inputs.vehicle.UpdateVehicleDto;
import com.example.esperar_app.persistence.entity.Vehicle;
import com.example.esperar_app.persistence.entity.company.Company;
import com.example.esperar_app.persistence.entity.security.User;
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

import java.beans.PropertyDescriptor;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;
    private final UserRepository userRepository;

    @Autowired
    public VehicleServiceImpl(
            VehicleRepository vehicleRepository,
            VehicleMapper vehicleMapper,
            UserRepository userRepository) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleMapper = vehicleMapper;
        this.userRepository = userRepository;
    }

    @Override
    public Vehicle create(CreateVehicleDto createVehicleDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("Owner not found"));

        Vehicle vehicle = vehicleMapper.toEntity(createVehicleDto);

        vehicle.setOwner(owner);

        vehicle.setDriver(userRepository.findById(createVehicleDto.getDriverId())
                .orElseThrow(() -> new ObjectNotFoundException("Driver not found")));

        try {
            Company company = owner.getCompanies().get(0);
            vehicle.setCompany(company);
        } catch (Exception e) {
            throw new ObjectNotFoundException
                    ("No tienes una empresa creada, para crear un vehículo primero crea una empresa");
        }

        return vehicleRepository.save(vehicle);
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

        return vehicleMapper.toGetVehicle(vehicleFound);
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
