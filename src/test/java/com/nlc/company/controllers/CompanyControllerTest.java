package com.nlc.company.controllers;

import com.nlc.company.errors.CompanyNotFoundException;
import com.nlc.company.repositories.CompanyRepository;
import com.nlc.company.resources.Company;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
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
        assert response.getStatusCodeValue() == 201;
        assert response.getHeaders().get("Location").get(0).equals(LOCATION_URL);
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
        assert response.getStatusCodeValue() == 201;
        assert response.getHeaders().get("Location").get(0).equals(LOCATION_URL);
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
        assert allCompanies != null;
        assert allCompanies.size() > 0;
        assert allCompanies.size() == 1;
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
        assert result != null;
        assert result.getId().equals(companyId);
    }

    @Test(expected = CompanyNotFoundException.class)
    public void givenAnIdThatDoesNotExistReturnNotFoundError() {
        //given
        CompanyController controller = new CompanyControllerForTest(repositoryMocked);
        Long companyId = 1L;

        //when
        controller.getCompanyById(companyId);
    }

    private Company createCompany(Boolean withOptionals) {
        return new Company(1L, "Apple, INC", "Infinity Loop 1", "Palo Alto", "California",
                withOptionals ? "info@apple.com" : null, withOptionals ? "+1 333 555 7830" : null, new ArrayList<>());
    }

}