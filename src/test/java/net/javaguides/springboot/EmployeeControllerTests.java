package net.javaguides.springboot;

import net.javaguides.springboot.controller.EmployeeController;
import net.javaguides.springboot.dto.EmployeeDto;
import net.javaguides.springboot.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;

import static org.mockito.BDDMockito.given;
import static reactor.core.publisher.Mono.just;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;


@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = EmployeeController.class) // loads only Employee Controller beans, resulting in faster tests
public class EmployeeControllerTests {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private EmployeeService employeeService;

    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnSavedEmployee(){

        // given - recondition or setup
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Ioan");
        employeeDto.setLastName("Dorel");
        employeeDto.setEmail("ioan@dsfas.com");

        given(employeeService.saveEmployee(ArgumentMatchers.any(EmployeeDto.class)))
                .willReturn(just(employeeDto));

        // when - action or behavior
        WebTestClient.ResponseSpec response = webTestClient.post().uri("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(just(employeeDto), EmployeeDto.class)
                .exchange();

        // then - verify the result
        response.expectStatus().isCreated()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }
}
