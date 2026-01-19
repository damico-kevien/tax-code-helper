package com.challenge.tax_code_helper.service;

import com.challenge.tax_code_helper.model.TaxCodeInfo;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@NoArgsConstructor
@Slf4j
public class TaxCodeHelperService {

    private static final Pattern TAX_CODE_PATTERN = Pattern.compile("^[A-Z]{6}([0-9]{2})([A-Z])([0-9]{2})[A-Z][0-9]{3}[A-Z]$");

    public TaxCodeInfo getInfo(String taxCode) {
        log.debug("Trying to validate tax code...");
        Matcher matcher = TAX_CODE_PATTERN.matcher(taxCode.toUpperCase());
        if (!matcher.find()) {
            log.error("Tax code not valid");
            throw new IllegalArgumentException("Tax code not valid");
        }
        log.debug("Tax code is valid. Extracting date of birth and age...");
        // Retrieve year
        int partialYear = Integer.parseInt(matcher.group(1));
        int year = this.getYear(partialYear);
        // Retrieve day
        int day = Integer.parseInt(matcher.group(3));
        boolean isFemale = day > 40;
        if (isFemale) {
            day -= 40;
        }
        // retrieve month
        char monthChar = matcher.group(2).charAt(0);
        int month = this.getMonth(monthChar);
        // calculate date of birth
        LocalDate dateOfBirth = LocalDate.of(year, month, day);
        // calculate age
        int age = Period.between(dateOfBirth, LocalDate.now()).getYears();
        log.debug("Done!");
        return new TaxCodeInfo(age, dateOfBirth);
    }

    private int getMonth(char monthChar) {
        int month;
        switch (monthChar) {
            case 'A' -> month = 1;
            case 'B' -> month = 2;
            case 'C' -> month = 3;
            case 'D' -> month = 4;
            case 'E' -> month = 5;
            case 'H' -> month = 6;
            case 'L' -> month = 7;
            case 'M' -> month = 8;
            case 'P' -> month = 9;
            case 'R' -> month = 10;
            case 'S' -> month = 11;
            case 'T' -> month = 12;
            default -> throw new IllegalArgumentException("Provided month in tax code not valid");
        }
        return month;
    }

    private int getYear(int partialYear) {
        int currentYear = LocalDate.now().getYear() % 100;
        return (partialYear <= currentYear) ? (2000 + partialYear) : (1900 + partialYear);
    }
}
