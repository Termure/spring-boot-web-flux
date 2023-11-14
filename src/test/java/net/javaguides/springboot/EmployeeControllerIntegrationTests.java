package net.javaguides.springboot;

import net.javaguides.springboot.dto.EmployeeDto;
import net.javaguides.springboot.repository.EmployeeRepository;
import net.javaguides.springboot.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Collections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerIntegrationTests {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    public void before(){
        System.out.println("Before each test");
        employeeRepository.deleteAll().subscribe();
    }

    @Test
    public void testSaveEmployee(){
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setEmail("fdsf");
        employeeDto.setLastName("fdsf");
        employeeDto.setFirstName("fdsf");

        webTestClient.post().uri("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(employeeDto), EmployeeDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }

    @Test
    public void givenEmployeeId_whenFindById_thenReturnTheEmployee(){
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("sfasf");
        employeeDto.setLastName("sfasf");
        employeeDto.setEmail("sfasf");

        EmployeeDto savedEmployee = employeeService.saveEmployee(employeeDto).block();

        webTestClient.get().uri("/api/employees/{id}", Collections.singletonMap("id", savedEmployee.getId()))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.email").isEqualTo(savedEmployee.getEmail());
    }

    @Test
    public void givenEmployeeList_whenGetAllEmployees_thenReturnTheList(){
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("sfasf");
        employeeDto.setLastName("sfasf");
        employeeDto.setEmail("sfasf");
        employeeService.saveEmployee(employeeDto).block();

        EmployeeDto employeeDto1 = new EmployeeDto();
        employeeDto1.setFirstName("sfasf");
        employeeDto1.setLastName("sfasf");
        employeeDto1.setEmail("sfasf");
        employeeService.saveEmployee(employeeDto1).block();

        webTestClient.get().uri("/api/employees")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EmployeeDto.class)
                .consumeWith(System.out::println);
    }

    @Test
    public void testUpdateEmployee(){
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("sfasf");
        employeeDto.setLastName("sfasf");
        employeeDto.setEmail("sfasf");
        EmployeeDto savedEmployee = employeeService.saveEmployee(employeeDto).block();

        EmployeeDto updatedEmployee = new EmployeeDto();
        updatedEmployee.setFirstName("sfasf");
        updatedEmployee.setLastName("sfasf");
        updatedEmployee.setEmail("sfasf");

        webTestClient.put().uri("/api/employees/{id}", Collections.singletonMap("id", savedEmployee.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedEmployee), EmployeeDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.email").isEqualTo(updatedEmployee.getEmail());

    }
}
