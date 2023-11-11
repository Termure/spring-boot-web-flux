package net.javaguides.springboot.service.impl;

import lombok.AllArgsConstructor;
import net.javaguides.springboot.dto.EmployeeDto;
import net.javaguides.springboot.entity.Employee;
import net.javaguides.springboot.mapper.EmployeeMapper;
import net.javaguides.springboot.repository.EmployeeRepository;
import net.javaguides.springboot.service.EmployeeService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;
    @Override
    public Mono<EmployeeDto> saveEmployee(EmployeeDto employeeDto){
        // convert EmployeeDto into Employee Entity
        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);
        Mono<Employee> savedEmployee = employeeRepository.save(employee);
        return savedEmployee
                .map((employeeEntity) -> EmployeeMapper.mapToEmployeeDto(employeeEntity));
    }
}
