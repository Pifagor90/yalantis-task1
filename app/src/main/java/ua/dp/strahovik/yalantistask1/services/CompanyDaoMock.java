/*
Copyright info
*/


package ua.dp.strahovik.yalantistask1.services;


import ua.dp.strahovik.yalantistask1.entities.Company;

public class CompanyDaoMock implements CompanyDao {

    @Override
    public Company getCompanyByName(String name) {
        return new Company(name);
    }
}
