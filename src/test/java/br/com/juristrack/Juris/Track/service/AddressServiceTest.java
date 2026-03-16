package br.com.juristrack.Juris.Track.service;

import br.com.juristrack.Juris.Track.dto.request.AddressRequest;
import br.com.juristrack.Juris.Track.mapper.AddressMapper;
import br.com.juristrack.Juris.Track.model.entity.Address;
import br.com.juristrack.Juris.Track.model.repository.AddressRepository;
import br.com.juristrack.Juris.Track.support.AddressSupport;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private AddressService addressService;

    @Captor
    private ArgumentCaptor<Address> addressArgumentCaptor;

    @Nested
    class BuildAddress {
        @Test
        void should_build_addressEntity_when_addressRequest_is_valid() {
            //Arrange
            AddressRequest addressRequest = AddressSupport.validRequest();
            Address address = AddressSupport.validEntity();

            when(addressMapper.toAddress(addressRequest)).thenReturn(address);

            //Act & Assert
            var result = assertDoesNotThrow(() -> addressService.buildAddress(addressRequest));

            assertEquals(addressRequest.street(), address.getStreet());
        }
    }

}