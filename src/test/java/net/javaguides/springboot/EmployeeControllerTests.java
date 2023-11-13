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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.print.attribute.standard.MediaSize;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = EmployeeController.class) // loads only Employee Controller beans, resulting in faster tests
public class EmployeeControllerTests {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private EmployeeService employeeService;

    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnSavedEmployee(){

        // given - precondition or setup
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

    @Test
    public void givenEmployeeId_whenGetById_thenReturnTheEmployee(){
        // given - precondition or setup
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId("FAFSA");
        employeeDto.setFirstName("fsfs");
        employeeDto.setLastName("adidas");
        employeeDto.setEmail("fdsafs@dfsaf.com");

        given(employeeService.getEmployee(employeeDto.getId()))
                .willReturn(just(employeeDto));

        // when - action or behavior
        WebTestClient.ResponseSpec response = webTestClient.get()
                .uri("/api/employees/{id}", Collections.singletonMap("id", employeeDto.getId()))
                .exchange();

        // the - verify the output
        response.expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }

    @Test
    public void givenEmployees_whenGetAll_thenReturnTheList(){
         List<EmployeeDto> list = new ArrayList<>();
         EmployeeDto employeeDto = new EmployeeDto();
         employeeDto.setLastName("dfasf");
         employeeDto.setLastName("dfasf");
         employeeDto.setEmail("dfasf");
         list.add(employeeDto);

         EmployeeDto employeeDto1 = new EmployeeDto();
         employeeDto1.setLastName("dfasf");
         employeeDto1.setLastName("dfasf");
         employeeDto1.setEmail("dfasf");
         list.add(employeeDto1);

         Flux<EmployeeDto> employeeFlux = Flux.fromIterable(list);

         given(employeeService.getAllEmployees()).willReturn(employeeFlux);

         WebTestClient.ResponseSpec response = webTestClient.get().uri("/api/employees")
                 .accept(MediaType.APPLICATION_JSON)
                 .exchange();

         response.expectStatus().isOk()
                 .expectBodyList(EmployeeDto.class)
                 .consumeWith(System.out::println);
    }

    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnTheEmployee(){
        String employeeId = "2234";

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setLastName("dfasf");
        employeeDto.setLastName("dfasf");
        employeeDto.setEmail("dfasf");

        given(employeeService.updateEmployee(ArgumentMatchers.any(EmployeeDto.class),
                ArgumentMatchers.any(String.class)))
                .willReturn(just(employeeDto));

        WebTestClient.ResponseSpec response = webTestClient.put().uri("/api/employees/{id}", Collections.singletonMap("id", employeeId))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(just(employeeDto), EmployeeDto.class)
                .exchange();

        response.expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }
}
