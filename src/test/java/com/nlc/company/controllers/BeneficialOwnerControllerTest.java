package com.nlc.company.controllers;

import com.nlc.company.errors.CompanyNotFoundException;
import com.nlc.company.repositories.BeneficialOwnerRepository;
import com.nlc.company.repositories.CompanyRepository;
import com.nlc.company.resources.BeneficialOwner;
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

public class BeneficialOwnerControllerTest {

    private static final String LOCATION_URL = "https://beneficialOwner.com";
    private CompanyRepository companyRepository;
    private BeneficialOwnerRepository beneficialOwnerRepository;
    private Long companyId = 1L;
    private ServletUriComponentsBuilder servletMocked;
    private UriComponentsBuilder uriComponentBuilderMocked;
    private UriComponents uriComponentsMocked;

    class BeneficialOwnerControllerForTest extends BeneficialOwnerController {

        public BeneficialOwnerControllerForTest(CompanyRepository companyRepository, BeneficialOwnerRepository beneficialOwnerRepository) {
            super(companyRepository, beneficialOwnerRepository);
        }

        @Override
        protected ServletUriComponentsBuilder getCurrentContextPath() {
            return servletMocked;
        }
    }

    @Before
    public void setUp() throws Exception {
        companyRepository = mock(CompanyRepository.class);
        servletMocked = mock(ServletUriComponentsBuilder.class);
        uriComponentBuilderMocked = mock(UriComponentsBuilder.class);
        uriComponentsMocked = mock(UriComponents.class);
        beneficialOwnerRepository = mock(BeneficialOwnerRepository.class);

        given(servletMocked.path(anyString())).willReturn(uriComponentBuilderMocked);
        given(uriComponentBuilderMocked.path(anyString())).willReturn(uriComponentBuilderMocked);
        given(uriComponentBuilderMocked.buildAndExpand(anyLong(), anyLong())).willReturn(uriComponentsMocked);
        given(uriComponentsMocked.toUri()).willReturn(URI.create(LOCATION_URL));
    }

    @Test
    public void allBenificialOwnersAreReturnedFromTheGetterService() {
        //given
        BeneficialOwnerController controller = new BeneficialOwnerControllerForTest(companyRepository, beneficialOwnerRepository);
        Optional<Company> optionalCompany = Optional.of(createCompany(true));
        given(companyRepository.findById(companyId)).willReturn(optionalCompany);

        //when
        Collection<BeneficialOwner> result = controller.getAllBeneficialOwners(companyId);

        //then
        assertThat("result size", result.size(), greaterThan(0));
    }

    @Test(expected=CompanyNotFoundException.class)
    public void notFoundErrorIfIdDoesNotExist() {
        //given
        BeneficialOwnerController controller = new BeneficialOwnerControllerForTest(companyRepository, beneficialOwnerRepository);

        //when
        Collection<BeneficialOwner> result = controller.getAllBeneficialOwners(0L);
    }

    @Test
    public void newlyCreatedBeneficialOwnerReturnsHttpStatus201() {
        //given
        BeneficialOwnerController controller = new BeneficialOwnerControllerForTest(companyRepository, beneficialOwnerRepository);
        Optional<Company> optionalCompany = Optional.of(createCompany(true));
        given(companyRepository.findById(companyId)).willReturn(optionalCompany);
        BeneficialOwner newBeneficialOwner = createBeneficialOwner(createCompany(false), 4);

        given(beneficialOwnerRepository.save(any())).willReturn(newBeneficialOwner);

        //when
        ResponseEntity result = controller.createBeneficialOwner(1L, newBeneficialOwner);

        //then
        Assert.assertEquals(201, result.getStatusCodeValue());
        Assert.assertEquals(LOCATION_URL, result.getHeaders().get("Location").get(0));

        InOrder inOrder = inOrder(servletMocked, uriComponentBuilderMocked);

        inOrder.verify(servletMocked).path("/companies");
        inOrder.verify(uriComponentBuilderMocked).path("/{companyId}");
        inOrder.verify(uriComponentBuilderMocked).path("/beneficialOwners");
        inOrder.verify(uriComponentBuilderMocked).path("/{beneficialOwnerId}");
    }

    private Company createCompany(Boolean withOptionals) {
        Company company = new Company(1L, "Apple, INC", "Infinity Loop 1", "Palo Alto", "California",
                withOptionals ? "info@apple.com" : null, withOptionals ? "+1 333 555 7830" : null, null);

        company.setBeneficialOwners(getBeneficialOwnersList(company));

        return company;
    }

    private List<BeneficialOwner> getBeneficialOwnersList(Company company) {
        List<BeneficialOwner> beneficialOwnersList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            BeneficialOwner temp = createBeneficialOwner(company, i);
            beneficialOwnersList.add(temp);
        }

        return beneficialOwnersList;
    }

    private BeneficialOwner createBeneficialOwner(Company company, int newBeneficialOwnerId) {
        BeneficialOwner temp = new BeneficialOwner();
        temp.setCompany(company);
        temp.setId(new Long(newBeneficialOwnerId));
        temp.setName("beneficial owner " + newBeneficialOwnerId);
        return temp;
    }
}