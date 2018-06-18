package com.nlc.company.controllers;

import com.nlc.company.errors.CompanyNotFoundException;
import com.nlc.company.repositories.CompanyRepository;
import com.nlc.company.resources.Company;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

public class CompanyControllerTest {

    private static final String LOCATION_URL = "https://uri.com";
    private UriComponentsBuilder uriComponentBuilderMocked;
    private CompanyRepository repositoryMocked;
    private ServletUriComponentsBuilder servletUrilMocked;
    private UriComponents uriComponentsMocked;

    class CompanyControllerForTest extends CompanyController {

        public CompanyControllerForTest(CompanyRepository companyRepository) {
            super(companyRepository);
        }

        @Override
        protected ServletUriComponentsBuilder getCurrentContextPath() {
            return servletUrilMocked;
        }
    }

    @Before
    public void setUp() throws Exception {
        repositoryMocked = mock(CompanyRepository.class);
        servletUrilMocked = mock(ServletUriComponentsBuilder.class);
        uriComponentBuilderMocked = mock(UriComponentsBuilder.class);
        uriComponentsMocked = mock(UriComponents.class);

        given(servletUrilMocked.path(anyString())).willReturn(uriComponentBuilderMocked);
        given(uriComponentBuilderMocked.path(anyString())).willReturn(uriComponentBuilderMocked);
        given(uriComponentBuilderMocked.buildAndExpand(anyLong())).willReturn(uriComponentsMocked);
        given(uriComponentsMocked.toUri()).willReturn(URI.create(LOCATION_URL));
    }

    @Test
    public void createNewCompanyReturnsACompanyObject() {
        //given
        CompanyController controller = new CompanyControllerForTest(repositoryMocked);
        Company companyToCreate = createCompany(true);
        given(repositoryMocked.save(any())).willReturn(companyToCreate);

        //when
        ResponseEntity response = controller.createNewCompany(companyToCreate);

        //then
        Assert.assertEquals(201, response.getStatusCodeValue());
        Assert.assertEquals(LOCATION_URL, response.getHeaders().get("Location").get(0));

        InOrder inOrder = inOrder(servletUrilMocked, uriComponentBuilderMocked);

        inOrder.verify(servletUrilMocked).path("/companies");
        inOrder.verify(uriComponentBuilderMocked).path("/{id}");
    }

    @Test
    public void createNewCompanyWithoutOptionalParameters() {
        //given
        CompanyController controller = new CompanyControllerForTest(repositoryMocked);
        Company companyToCreate = createCompany(false);
        given(repositoryMocked.save(any())).willReturn(companyToCreate);

        //when
        ResponseEntity response = controller.createNewCompany(companyToCreate);

        //then
        Assert.assertEquals(201, response.getStatusCodeValue());
        Assert.assertEquals(LOCATION_URL, response.getHeaders().get("Location").get(0));
    }

    @Test
    public void getAllCompaniesReturnAllCompanies() {
        //given
        CompanyController controller = new CompanyControllerForTest(repositoryMocked);
        List<Company> companies = new ArrayList<>();
        companies.add(createCompany(true));
        given(repositoryMocked.findAll()).willReturn(companies);

        //when
        Collection<Company> allCompanies = controller.getAllCompanies();

        //then
        Assert.assertNotNull(allCompanies);

        assertThat("company size",
                allCompanies.size(),
                greaterThan(0));

        Assert.assertEquals(1, allCompanies.size());
    }

    @Test
    public void givenAnIdReturnTheCompanyFound() {
        //given
        CompanyController controller = new CompanyControllerForTest(repositoryMocked);
        Long companyId = 1L;
        Company company = createCompany(true);
        company.setId(companyId);

        Optional<Company> companyOptional = Optional.of(company);
        given(repositoryMocked.findById(1L)).willReturn(companyOptional);

        //when
        Company result = controller.getCompanyById(companyId);

        //then
        Assert.assertNotNull(result);
        Assert.assertEquals(companyId, result.getId());
    }

    @Test(expected = CompanyNotFoundException.class)
    public void givenAnIdThatDoesNotExistReturnNotFoundError() {
        //given
        CompanyController controller = new CompanyControllerForTest(repositoryMocked);
        Long companyId = 1L;

        //when
        controller.getCompanyById(companyId);
    }

    @Test
    public void companyWithGivenIdIsModified() {
        //given
        CompanyController controller = new CompanyControllerForTest(repositoryMocked);
        Long companyId = 1L;
        Company company = createCompanyWithId(companyId);
        Optional<Company> optionalCompany = Optional.of(company);
        given(repositoryMocked.findById(companyId)).willReturn(optionalCompany);

        Company newCompanyData = createCompanyWithId(companyId);
        newCompanyData.setCity("London");
        newCompanyData.setName("compantName");
        newCompanyData.setAddress("Mountain view St.");
        newCompanyData.setCountry("UK");
        newCompanyData.setEmail("newComp@comp.com");
        newCompanyData.setPhone("+44 979307373");

        //when
        Company result = controller.updateCompany(companyId, newCompanyData);

        //then
        Assert.assertNotNull(result);
        Assert.assertEquals("London", result.getCity());
        Assert.assertEquals("compantName", result.getName());
        Assert.assertEquals("Mountain view St.", result.getAddress());
        Assert.assertEquals("UK", result.getCountry());
        Assert.assertEquals("newComp@comp.com", result.getEmail());
        Assert.assertEquals("+44 979307373", result.getPhone());
    }

    @Test(expected=CompanyNotFoundException.class)
    public void returnsNotFoundErrorIfIdDoesNotExist() {
        //given
        CompanyController controller = new CompanyControllerForTest(repositoryMocked);
        Long companyId = 1L;

        Company newCompanyData = createCompanyWithId(companyId);
        newCompanyData.setCity("London");
        newCompanyData.setAddress("Mountain view St.");
        newCompanyData.setCountry("UK");
        newCompanyData.setEmail("newComp@comp.com");
        newCompanyData.setPhone("+44 979307373");

        //when
        controller.updateCompany(companyId, newCompanyData);
    }

    private Company createCompanyWithId(long id) {
        Company company = createCompany(true);
        company.setId(id);

        return company;
    }

    private Company createCompany(Boolean withOptionals) {
        return new Company(1L, "Apple, INC", "Infinity Loop 1", "Palo Alto", "California",
                withOptionals ? "info@apple.com" : null, withOptionals ? "+1 333 555 7830" : null, new ArrayList<>());
    }

}