package com.odenktools.master.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.odenktools.common.dto.CustomerDto;
import com.odenktools.common.dto.CustomerGroupDto;
import com.odenktools.common.dto.ProductDto;
import com.odenktools.common.model.CustomerGroup;
import com.odenktools.common.response.ApiResponse;
import com.odenktools.common.response.DataObjectResponse;
import com.odenktools.common.response.RestApiResponse;
import com.odenktools.common.util.RequestUtils;
import com.odenktools.master.service.CustomerGroupService;
import com.odenktools.master.service.CustomerService;
import com.odenktools.master.service.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class CustomerGroupController {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerGroupController.class);

    /**
     * (1) DependencyInjection Fields.
     */
    private final CustomerGroupService customerGroupService;
    private final GroupService groupService;
    private final CustomerService customerService;
    private final RestTemplate restTemplate;

    @Autowired
    public CustomerGroupController(CustomerGroupService customerGroupService, GroupService groupService, CustomerService customerService, RestTemplate restTemplate) {
        this.customerGroupService = customerGroupService;
        this.groupService = groupService;
        this.customerService = customerService;
        this.restTemplate = restTemplate;
    }

    /**
     * @param request
     * @param httpServletRequest
     * @return
     */
    @PostMapping(
            value = "/customer/group/create",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> registerCustomerToGroup(@RequestBody @Valid CustomerGroupDto request,
                                         HttpServletRequest httpServletRequest) {

        DataObjectResponse<CustomerGroupDto> content = new DataObjectResponse<>();
        String messages = "";

        boolean isValidContraint = true;

//        if (!customerService.existsById(request.getCustomerId())) {
//
//            messages = String.format("Customer id is not found");
//
//            content.setContent(null);
//            content.setCode(HttpStatus.CONFLICT.value());
//            content.setMessage(messages);
//
//            isValidContraint = false;
//
//        }

        ResponseEntity<RestApiResponse<CustomerDto>> customerResponse;
        String customerId = null;

        try {
            customerResponse = this.restTemplate.exchange(
                    String.format("http://localhost:6000/api/v1/customer?id=%s", request.getCustomerId()),
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<RestApiResponse<CustomerDto>>() {
                    });
            LOG.info(customerResponse.getBody().getApiVersion());
            customerId = Objects.requireNonNull(customerResponse.getBody().getResults().getData().getId());

        } catch (HttpClientErrorException e) {
            LOG.error("ERROR {}", e.getMessage());
        }

        if (customerId == null) {

            messages = String.format("Customer id is not found");

            content.setContent(null);
            content.setCode(HttpStatus.CONFLICT.value());
            content.setMessage(messages);

            isValidContraint = false;
        }

        if (!groupService.existsById(request.getGroupId())) {

            messages = String.format("Group id is not found");

            content.setContent(null);
            content.setCode(HttpStatus.CONFLICT.value());
            content.setMessage(messages);

            isValidContraint = false;

        }
        if (this.customerGroupService.existsByGroupId(request.getGroupId()) &&
                this.customerGroupService.existsByCustomerId(request.getCustomerId())) {

            messages = String.format("Customer is already registered to this Group");

            content.setContent(null);
            content.setCode(HttpStatus.CONFLICT.value());
            content.setMessage(messages);

            isValidContraint = false;

        }

        if (isValidContraint) {
            messages = "Create Realtion Group success.";
            content.setContent(null);
            content.setCode(HttpStatus.OK.value());
            content.setMessage(messages);
            content.setData(this.customerGroupService.registerCustomerGroup(request));

            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                    .status(HttpStatus.OK)
                    .code(HttpStatus.OK.value())
                    .message(messages)
                    .params(null)
                    .method(httpServletRequest.getMethod())
                    .path(httpServletRequest.getRequestURI())
                    .results(content)
                    .build());

        } else {

            LOG.error("VALIDATION ERROR {}", messages);

            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    ApiResponse.builder()
                            .status(HttpStatus.CONFLICT)
                            .code(HttpStatus.CONFLICT.value())
                            .message(messages)
                            .params(null)
                            .method(httpServletRequest.getMethod())
                            .path(httpServletRequest.getRequestURI())
                            .results(content)
                            .build());
        }
    }


    @RequestMapping(value = "/customer/group/list",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAllData(@RequestParam(name = "name", defaultValue = "") String name,
                                         @RequestParam(required = false, defaultValue = "0") Integer page,
                                         @RequestParam(required = false, defaultValue = "10") Integer size,
                                         @RequestParam(required = false, defaultValue = "DESC") String direction,
                                         HttpServletRequest httpServletRequest) {

        List<String> sortProperties = new ArrayList<>();
        sortProperties.add("createdAt");
        Sort sort = new Sort(Sort.Direction.fromString(direction), sortProperties);

        Page<CustomerGroup> listData = this.customerGroupService
                .findAllBy(name,
                        sort, PageRequest.of(page, size));

        List<CustomerGroup> dataList = listData.getContent();

        DataObjectResponse<CustomerGroup> content = new DataObjectResponse<>();
        content.setContent(dataList);
        content.setCode(HttpStatus.OK.value());
        content.setMessage("Ok!");

        ObjectNode objectNode = RequestUtils.getAllParams(httpServletRequest);

        LOG.info("HITTING PRODUCT_LIST PAGE =[{}], SIZE {}", page, size);

        if (objectNode != null) {
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                    .status(HttpStatus.OK)
                    .code(HttpStatus.OK.value())
                    .message("success.")
                    .params(objectNode)
                    .pageable(listData.getPageable())
                    .method(httpServletRequest.getMethod())
                    .path(httpServletRequest.getRequestURI())
                    .results(content)
                    .build()
            );
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                    .status(HttpStatus.OK)
                    .code(HttpStatus.OK.value())
                    .params(null)
                    .pageable(listData.getPageable())
                    .method(httpServletRequest.getMethod())
                    .path(httpServletRequest.getRequestURI())
                    .results(content)
                    .build()
            );
        }
    }

    /**
     * Get Group detail.
     *
     * @param id id do you want to check.
     * @return GroupDto
     */
    @GetMapping(value = "/customer/group",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findGroupById(@RequestParam("id") String id,
                                           HttpServletRequest httpServletRequest) {

        DataObjectResponse<CustomerGroup> content = new DataObjectResponse<>();
        String messages = "";

        Optional<CustomerGroup> customerGroupOptional = this.customerGroupService.findById(id);

        if (customerGroupOptional.isPresent()) {

            CustomerGroup customerGroup = customerGroupOptional.get();

            messages = "success.";
            content.setContent(null);
            content.setCode(HttpStatus.OK.value());
            content.setMessage(messages);
            content.setData(customerGroup);

            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                    .status(HttpStatus.OK)
                    .code(HttpStatus.OK.value())
                    .message(messages)
                    .params(null)
                    .method(httpServletRequest.getMethod())
                    .path(httpServletRequest.getRequestURI())
                    .results(content)
                    .build());
        }

        messages = "success.";
        content.setContent(null);
        content.setCode(HttpStatus.BAD_REQUEST.value());
        content.setMessage(messages);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .code(HttpStatus.BAD_REQUEST.value())
                .message(messages)
                .params(null)
                .method(httpServletRequest.getMethod())
                .path(httpServletRequest.getRequestURI())
                .results(content)
                .build());
    }

    /*@PostMapping(
            value = "/group/update",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> updateGroup(@RequestBody @Valid CustomerGroupDto request,
                                            HttpServletRequest httpServletRequest) {

        DataObjectResponse<GroupDto> content = new DataObjectResponse<>();
        String messages = "";

        boolean isValidContraint = true;

        if (!this.customerGroupService.existsById(request.getId())) {

            messages = String.format("Group with id ``%s`` is not found",
                    request.getId());

            content.setContent(null);
            content.setCode(HttpStatus.CONFLICT.value());
            content.setMessage(messages);

            isValidContraint = false;

        } else if (this.groupService.existsByName(request.getName())) {

            messages = String.format("Group with name ``%s`` already exist",
                    request.getName());

            content.setContent(null);
            content.setCode(HttpStatus.CONFLICT.value());
            content.setMessage(messages);

            isValidContraint = false;

        }

        if (isValidContraint) {
            messages = "Update Group success.";
            content.setContent(null);
            content.setCode(HttpStatus.OK.value());
            content.setMessage(messages);
            content.setData(this.groupService.updateGroup(request));

            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                    .status(HttpStatus.OK)
                    .code(HttpStatus.OK.value())
                    .message(messages)
                    .params(null)
                    .method(httpServletRequest.getMethod())
                    .path(httpServletRequest.getRequestURI())
                    .results(content)
                    .build());

        } else {

            LOG.error("VALIDATION ERROR {}", messages);

            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    ApiResponse.builder()
                            .status(HttpStatus.CONFLICT)
                            .code(HttpStatus.CONFLICT.value())
                            .message(messages)
                            .params(null)
                            .method(httpServletRequest.getMethod())
                            .path(httpServletRequest.getRequestURI())
                            .results(content)
                            .build());
        }
    }*/

}
