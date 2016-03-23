/*
Copyright info
*/


package ua.dp.strahovik.yalantistask1.services;

import ua.dp.strahovik.yalantistask1.entities.Company;

public interface CompanyDao {


    public Company getCompanyByName(String name);
}
