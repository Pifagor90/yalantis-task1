/*
Copyright info
*/


package ua.dp.strahovik.yalantistask1.services;


import ua.dp.strahovik.yalantistask1.entities.Company;

public class CompanyFactory {

    public static Company getCompanyByName(String name) {
        return new Company(name);
    }
}
