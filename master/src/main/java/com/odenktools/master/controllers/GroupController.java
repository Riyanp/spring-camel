package com.odenktools.master.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.odenktools.common.dto.GroupDto;
import com.odenktools.common.model.Group;
import com.odenktools.common.response.ApiResponse;
import com.odenktools.common.response.DataObjectResponse;
import com.odenktools.common.util.RequestUtils;
import com.odenktools.master.service.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class GroupController {

    private static final Logger LOG = LoggerFactory.getLogger(GroupController.class);

    /**
     * (1) DependencyInjection Fields.
     */
    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    /**
     * @param request
     * @param httpServletRequest
     * @return
     */
    @PostMapping(
            value = "/group/create",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> createNewGroup(@RequestBody @Valid GroupDto request,
                                         HttpServletRequest httpServletRequest) {

        DataObjectResponse<GroupDto> content = new DataObjectResponse<>();
        String messages = "";

        boolean isValidContraint = true;

        if (this.groupService.existsByName(request.getName())) {

            messages = String.format("Group with name ``%s`` already exist",
                    request.getName());

            content.setContent(null);
            content.setCode(HttpStatus.CONFLICT.value());
            content.setMessage(messages);

            isValidContraint = false;

        }

        if (isValidContraint) {
            messages = "Create Group success.";
            content.setContent(null);
            content.setCode(HttpStatus.OK.value());
            content.setMessage(messages);
            content.setData(this.groupService.createGroup(request));

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

    /**
     * List all group .
     * @param name
     * @param page
     * @param size
     * @param direction
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/group/list",
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

        Page<Group> listData = this.groupService
                .findAllBy(name,
                        sort, PageRequest.of(page, size));

        List<Group> dataList = listData.getContent();

        DataObjectResponse<Group> content = new DataObjectResponse<>();
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
    @GetMapping(value = "/group",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findGroupById(@RequestParam("id") String id,
                                           HttpServletRequest httpServletRequest) {

        DataObjectResponse<Group> content = new DataObjectResponse<>();
        String messages = "";

        Optional<Group> groupOptional = this.groupService.findById(id);

        if (groupOptional.isPresent()) {

            Group group = groupOptional.get();

            messages = "success.";
            content.setContent(null);
            content.setCode(HttpStatus.OK.value());
            content.setMessage(messages);
            content.setData(group);

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

    /**
     * Update group .
     * @param request
     * @param httpServletRequest
     * @return
     */
    @PostMapping(
            value = "/group/update",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> updateGroup(@RequestBody @Valid GroupDto request,
                                         HttpServletRequest httpServletRequest) {

        DataObjectResponse<GroupDto> content = new DataObjectResponse<>();
        String messages = "";

        boolean isValidContraint = true;

        if (!this.groupService.existsById(request.getId())) {

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
    }

    /**
     * Soft delete group .
     * @param request
     * @param httpServletRequest
     * @return
     */
    @DeleteMapping(
            value = "/group/delete/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> softDelete(@PathVariable("id") String id,
                                            HttpServletRequest httpServletRequest) {

        DataObjectResponse<Boolean> content = new DataObjectResponse<>();
        String messages = "";

        boolean isValidContraint = true;

        if (!this.groupService.existsById(id)) {

            messages = String.format("Group with id ``%s`` is not found",
                    id);

            content.setContent(null);
            content.setCode(HttpStatus.CONFLICT.value());
            content.setMessage(messages);

            isValidContraint = false;

        }

        if (isValidContraint) {
            messages = "Delete (soft) Group success.";
            content.setContent(null);
            content.setCode(HttpStatus.OK.value());
            content.setMessage(messages);
            content.setData(this.groupService.softDeleteGroup(id));

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

    /**
     * Hard delete group .
     * @param id
     * @param httpServletRequest
     * @return
     */
    @DeleteMapping(
            value = "/group/hard-delete/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> hardDelete(@PathVariable("id") String id,
                                            HttpServletRequest httpServletRequest) {

        DataObjectResponse<Boolean> content = new DataObjectResponse<>();
        String messages = "";

        boolean isValidContraint = true;

        if (!this.groupService.existsById(id)) {

            messages = String.format("Group with id ``%s`` is not found",
                    id);

            content.setContent(null);
            content.setCode(HttpStatus.CONFLICT.value());
            content.setMessage(messages);

            isValidContraint = false;

        }

        if (isValidContraint) {
            messages = "Delete (hard) Group success.";
            content.setContent(null);
            content.setCode(HttpStatus.OK.value());
            content.setMessage(messages);
            content.setData(this.groupService.hardDeleteGroup(id));

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
}
