package com.example.desafionetdeal.controller;

import com.example.desafionetdeal.dto.EmployeeDTO;
import com.example.desafionetdeal.entity.Employee;
import com.example.desafionetdeal.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/save-employee")
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody EmployeeDTO employee) {
        return new ResponseEntity<>(employeeService.saveEmployee(employee), HttpStatus.CREATED);

    }

    @GetMapping
    public ResponseEntity<List<Employee>> listEmployees() {
        return ResponseEntity.ok(employeeService.listEmployees());
    }

    @GetMapping("/get-employee/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @PutMapping("/update-employee/{currentEmployeeId}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long currentEmployeeId, @RequestBody EmployeeDTO updatedEmployee) {
        return ResponseEntity.ok(employeeService.updateEmployee(currentEmployeeId, updatedEmployee));
    }

    @DeleteMapping("/delete-employee/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
