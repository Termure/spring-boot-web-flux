package net.javaguides.springboot;

import net.javaguides.springboot.dto.EmployeeDto;
import net.javaguides.springboot.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerIntegrationTests {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private WebTestClient webTestClient;

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
}
