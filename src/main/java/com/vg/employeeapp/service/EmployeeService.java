package com.vg.employeeapp.service;

import com.vg.employeeapp.entity.Employee;
import com.vg.employeeapp.response.AddressResponse;
import com.vg.employeeapp.response.EmployeeResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import com.vg.employeeapp.repo.EmployeeRepository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private WebClient webClient;

    //private RestTemplate restTemplate;

    /**public EmployeeService(@Value("${addressservice.base.url}") String addressBaseURL, RestTemplateBuilder builder) {
        restTemplate = builder.rootUri(addressBaseURL).build();
    }**/

    public EmployeeResponse getEmployeeById(int id) {
        Employee employee = employeeRepository.findById(id).get();
        EmployeeResponse employeeResponse = modelMapper.map(employee, EmployeeResponse.class);
        AddressResponse addressResponse = webClient
                                            .get()
                                            .uri("/address/"+id)
                                            .retrieve()
                                            .bodyToMono(AddressResponse.class)
                                            .block();
        //restTemplate.getForObject("/address/{id}", AddressResponse.class, id);

        employeeResponse.setAddressResponse(addressResponse);
        return employeeResponse;
    }
}
