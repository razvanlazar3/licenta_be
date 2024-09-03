package com.example.demo.banks.service;

import com.example.demo.banks.data.dtos.BankDTO;
import com.example.demo.banks.data.dtos.BankDepositRequestDTO;
import com.example.demo.banks.data.entities.BankDepositEntity;
import com.example.demo.banks.data.entities.BankEntity;
import com.example.demo.banks.data.entities.BankOfferEntity;
import com.example.demo.banks.data.mappers.BankDepositEntityMapper;
import com.example.demo.banks.data.mappers.BankEntityMapper;
import com.example.demo.banks.data.repos.BankDepositRepo;
import com.example.demo.banks.data.repos.BankOfferRepo;
import com.example.demo.banks.data.repos.BankRepo;
import com.example.demo.users.data.entities.UserEntity;
import com.example.demo.users.data.repos.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.demo.banks.data.constants.BankConstants.*;
import static com.example.demo.banks.data.factories.BankEntityFactory.buildBankEntity;
import static com.example.demo.banks.data.factories.BankOfferEntityFactory.buildBankOfferEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankServiceTest {

    @Mock
    private BankRepo bankRepo;

    @Mock
    private BankOfferRepo bankOfferRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private BankDepositRepo bankDepositRepo;

    @Mock
    private BankDepositEntityMapper bankDepositEntityMapper;

    @Mock
    private BankEntityMapper bankEntityMapper;

    @InjectMocks
    private BankService bankService;

    @Test
    void testAddPredefinedBanksWithOffers_whenRepoIsEmpty() {
        // given
        var bankEntity = buildBankEntity(BT_BANK, BT_BANK_ADDRESS);
        when(bankRepo.findAll()).thenReturn(List.of());
        when(bankRepo.findFirstByName(any())).thenReturn(Optional.of(bankEntity));
        when(bankOfferRepo.save(any())).thenReturn(buildBankOfferEntity(bankEntity, DISCOUNT_3, PERIOD_1Y));

        // execute
        bankService.addPredefinedBanksWithOffers();

        // assert
        verify(bankRepo).findAll();
        verify(bankRepo, times(8)).save(any(BankEntity.class));
        verify(bankOfferRepo, times(12)).save(any(BankOfferEntity.class));
    }

    @Test
    void testRegisterOffer() {
        // given
        BankDepositRequestDTO depositRequest = BankDepositRequestDTO
                .builder()
                .offerId(2L)
                .amount(1000)
                .build();

        var user = UserEntity
                .builder()
                .deposits(new ArrayList<>())
                .id(1L)
                .build();

        var newDepositEntity = BankDepositEntity
                .builder()
                .id(3L)
                .amount(1000)
                .user(user)
                .offerId(2L)
                .build();

        var newUser = UserEntity
                .builder()
                .id(1L)
                .deposits(new ArrayList<>(List.of(newDepositEntity)))
                .build();

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(bankDepositEntityMapper.toEntity(depositRequest)).thenReturn(newDepositEntity);
        when(bankDepositRepo.save(any())).thenReturn(newDepositEntity);

        // execute
        Long depositId = bankService.registerOffer(depositRequest, 1L);

        // assert
        assertNotNull(depositId);
        assertEquals(3L, depositId);

        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepo).save(userCaptor.capture());
        assertThat(userCaptor.getValue()).usingRecursiveComparison()
                .ignoringFields("createdDate", "user")
                .isEqualTo(newUser);

        ArgumentCaptor<BankDepositEntity> depositCaptor = ArgumentCaptor.forClass(BankDepositEntity.class);
        verify(bankDepositRepo).save(depositCaptor.capture());
        assertThat(depositCaptor.getValue()).usingRecursiveComparison()
                .ignoringFields("createdDate", "user", "id")
                .isEqualTo(newDepositEntity);
    }

    @Test
    void testGetAll() {
        // given
        var bankEntity1 = new BankEntity();
        bankEntity1.setName("Bank A");
        var bankEntity2 = new BankEntity();
        bankEntity2.setName("Bank B");

        when(bankRepo.findAll()).thenReturn(List.of(bankEntity1, bankEntity2));
        when(bankEntityMapper.toDTO(any(BankEntity.class)))
                .thenAnswer(invocation -> {
                    BankEntity entity = invocation.getArgument(0);
                    BankDTO dto = new BankDTO();
                    dto.setName(entity.getName());
                    return dto;
                });

        // execute
        List<BankDTO> banks = bankService.getAll();

        // assert
        assertEquals(2, banks.size());
        assertEquals("Bank A", banks.get(0).getName());
        assertEquals("Bank B", banks.get(1).getName());
        verify(bankRepo, times(1)).findAll();
    }

    @Test
    void testRemoveDeposit() {
        // given
        var depositId = 5L;
        var userId = 1L;
        var depositEntity = BankDepositEntity
                .builder()
                .id(depositId)
                .amount(1000)
                .build();

        var user = UserEntity
                .builder()
                .deposits(new ArrayList<>(List.of(depositEntity)))
                .id(userId)
                .build();

        var expectedUser = UserEntity
                .builder()
                .deposits(new ArrayList<>())
                .id(userId)
                .build();

        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(bankDepositRepo.findById(depositId)).thenReturn(Optional.of(depositEntity));

        // execute
        bankService.removeDeposit(depositId, userId);

        // assert
        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepo).save(userCaptor.capture());
        assertThat(userCaptor.getValue()).usingRecursiveComparison()
                .ignoringFields("createdDate", "user")
                .isEqualTo(expectedUser);
    }

    @Test
    void testGenerateReport() {
        // given
        BankEntity bank = BankEntity
                .builder()
                .name("Test Bank")
                .build();

        BankOfferEntity offer = new BankOfferEntity();
        offer.setBank(bank);
        offer.setPeriod(Period.ofYears(1));
        offer.setDiscount(10);

        LocalDateTime createdDate = LocalDateTime.now().minusYears(1);

        BankDepositEntity deposit = new BankDepositEntity();
        deposit.setAmount(1000);
        deposit.setCreatedDate(createdDate);
        deposit.setOfferId(1L);

        when(bankDepositRepo.findByUserId(1L)).thenReturn(List.of(deposit));
        when(bankOfferRepo.findById(1L)).thenReturn(Optional.of(offer));

        // execute
        String report = bankService.generateReport(1L);

        // assert
        assertNotNull(report);
        assertTrue(report.contains("mai aveti"));
        assertTrue(report.contains("Test Bank"));
        assertTrue(report.contains("1000"));

        int expectedFinalAmount = 1000 + (1000 * offer.getDiscount() / 100);
        long expectedRemainingDays = ChronoUnit.DAYS.between(LocalDate.now(), createdDate.plus(offer.getPeriod()));

        assertTrue(report.contains("dobanda si obtine suma finala de " + expectedFinalAmount));
        assertTrue(report.contains("mai aveti " + expectedRemainingDays + " zile pana la data"));

        verify(bankDepositRepo, times(1)).findByUserId(1L);
        verify(bankOfferRepo, times(1)).findById(1L);
    }
}
