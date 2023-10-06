package com.example.desafionetdeal.service;

import com.example.desafionetdeal.dto.EmployeeDTO;
import com.example.desafionetdeal.entity.Employee;
import com.example.desafionetdeal.exception.EmployeeNotFoundException;
import com.example.desafionetdeal.helper.EPasswordComplexity;
import com.example.desafionetdeal.helper.PasswordStrengthMeter;
import com.example.desafionetdeal.repository.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeService {

    private EmployeeRepository employeeRepository;

    private ModelMapper modelMapper;

    private PasswordEncoder passwordEncoder;

    public EmployeeService(EmployeeRepository employeeRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public Employee saveEmployee(EmployeeDTO employeeDto) {
        int score = PasswordStrengthMeter.calculatePasswordScore(employeeDto.getPassword());
        employeeDto.setPasswordComplexity(calculatePasswordComplexity(score));
        employeeDto.setPasswordScore(score);
        employeeDto.setPassword(passwordEncoder.encode(employeeDto.getPassword()));
        return employeeRepository.save(convertFromDto(employeeDto));
    }

    private String calculatePasswordComplexity(int score) {
        if(score < 25){
            return EPasswordComplexity.RUIM.name();
        } else if (score > 25 && score < 50){
            return EPasswordComplexity.MEDIANA.name();
        } else if (score > 50 && score < 75){
            return EPasswordComplexity.BOM.name();
        } else {
            return EPasswordComplexity.FORTE.name();
        }
    }

    public List<Employee> listEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException("Employee does not exist"));
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    public Employee updateEmployee(Long currentEmployeeId, EmployeeDTO newEmployee) {
        Employee employee = getEmployeeById(currentEmployeeId);

        if (employee != null) {
            employee.setName(newEmployee.getName());
            employee.setPassword(newEmployee.getPassword());
        }
        employee.setUpdatedAt(LocalDateTime.now());
        return saveEmployee(convertFromEntity(employee));
    }

    private Employee convertFromDto(EmployeeDTO employeeDto){
        return modelMapper.map(employeeDto,Employee.class);
    }

    private EmployeeDTO convertFromEntity(Employee employee){
        return modelMapper.map(employee,EmployeeDTO.class);
    }
}
