package com.challenge.tax_code_helper.service;

import com.challenge.tax_code_helper.model.TaxCodeInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TaxCodeHelperServiceTest {

    private TaxCodeHelperService service;

    @BeforeEach
    void setUp() {
        service = new TaxCodeHelperService();
    }

    @Test
    void getInfoMaleTaxCode() {
        // RSSMRA85M10H501Z - Mario Rossi, 10/08/1985
        String taxCode = "RSSMRA85M10H501Z";
        TaxCodeInfo info = service.getInfo(taxCode);
        assertNotNull(info);
        assertEquals(LocalDate.of(1985, 8, 10), info.dateOfBirth());
        assertTrue(info.age() > 0);
    }

    @Test
    void getInfoFemaleTaxCode() {
        String taxCode = "RSSMRA85M50H501Z";
        TaxCodeInfo info = service.getInfo(taxCode);
        assertNotNull(info);
        assertEquals(LocalDate.of(1985, 8, 10), info.dateOfBirth());
    }

    @Test
    void getInfoLowercaseTaxCode() {
        String taxCode = "rssmra85m10h501z";
        TaxCodeInfo info = service.getInfo(taxCode);
        assertNotNull(info);
        assertEquals(LocalDate.of(1985, 8, 10), info.dateOfBirth());
    }

    @Test
    void getInfoInvalidTaxCode() {
        String invalidTaxCode = "INVALID";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.getInfo(invalidTaxCode));
        assertEquals("Tax code not valid", exception.getMessage());
    }
}
