package org.example.smartshop.controller.client;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.smartshop.dto.client.ClientRequest;
import org.example.smartshop.dto.client.ClientResponse;
import org.example.smartshop.dto.client.ClientUpdate;
import org.example.smartshop.service.client.ClientService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@Tag(name = "Clients" , description = "Client management APIs")
@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientControllerImpl implements ClientController{
    private final ClientService clientService;


    @Operation(summary = "Create client")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Client Created Successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @PostMapping
    @Override
    public ResponseEntity<?> create(@Validated @RequestBody ClientRequest request){
          ClientResponse response = clientService.create(request);
          return ResponseEntity.status(HttpStatus.CREATED).body(
                  Map.of(
                          "message","Client Created Successfully",
                          "client", response
                  )
          );
    }
    @Operation(summary = "Update client")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Client Updated Successfully"),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @PutMapping("{clientId}")
    @Override
    public ResponseEntity<?> update(@Validated @RequestBody ClientUpdate request,@PathVariable String clientId){
          ClientResponse response = clientService.update(clientId,request);
          return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                  Map.of(
                          "message","Client Updated Successfully",
                          "Client", response
                  )
          );
    }

    @Operation(summary = "Delete client")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client Deleted Successfully"),
            @ApiResponse(responseCode = "404", description = "Client not found")
            })
    @DeleteMapping("{clientId}")
    @Override
    public ResponseEntity<?> delete(@PathVariable String clientId){
          clientService.remove(clientId);
          return ResponseEntity.ok().body(
                  Map.of(
                          "message","Client Deleted Successfully"
                  )
          );
    }

    @Operation(summary = "Get All clients")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Clients feched Successfully"),
    })
    @GetMapping
    @Override
    public ResponseEntity<Page<?>> getAll(@ParameterObject Pageable pageable){
          return ResponseEntity.ok().body( clientService.getAll(pageable));
    }

    @Operation(summary = "Get client By Id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client feched Successfully"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @GetMapping("{clientId}")
    @Override
    public ResponseEntity<?> getById(@PathVariable String clientId){
           Optional<ClientResponse> response= clientService.getById(clientId);
          return ResponseEntity.ok().body(
                  Map.of(
                          "message" , "Client feched Successfully",
                             "Client",response
                  ));
    }
}
