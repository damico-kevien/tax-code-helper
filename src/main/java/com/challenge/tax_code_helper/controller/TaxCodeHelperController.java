package com.challenge.tax_code_helper.controller;

import com.challenge.tax_code_helper.model.TaxCodeError;
import com.challenge.tax_code_helper.model.TaxCodeInfo;
import com.challenge.tax_code_helper.service.TaxCodeHelperService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.format.DateTimeFormatter;

@Controller
@Slf4j
@RequestMapping("/api/v1/tax-code")
@Tag(name = "Tax Code Helper", description = "API to extract information from tax code")
public class TaxCodeHelperController {

    private final TaxCodeHelperService service;

    public TaxCodeHelperController(TaxCodeHelperService service) {
        this.service = service;
    }

    @GetMapping("/{taxCode}")
    @Operation(summary = "Extract date of birth and age from tax code")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Information extracted successfully", content = @Content(schema = @Schema(implementation = TaxCodeInfo.class))),
            @ApiResponse(responseCode = "400", description = "Invalid tax code", content = @Content(schema = @Schema(implementation = TaxCodeError.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = TaxCodeError.class)))
    })
    public ResponseEntity<?> getInfoFromTaxCode(@PathVariable String taxCode) {
        TaxCodeInfo info;
        try {
            log.info("Retrieving information from tax code={}", taxCode.toUpperCase());
            info = service.getInfo(taxCode.toUpperCase());
        } catch (Exception e) {
            log.error("Error while retrieving information from tax code: {}", e.getMessage());
            if (e instanceof IllegalArgumentException) {
                return ResponseEntity.badRequest().body(new TaxCodeError(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
            }
            return ResponseEntity.internalServerError().body(new TaxCodeError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
        log.info("Info retrieved successfully: age={} | date of birth={}", info.age(), info.dateOfBirth().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        return ResponseEntity.ok(info);
    }
}
