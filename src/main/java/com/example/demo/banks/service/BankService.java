package com.example.demo.banks.service;

import com.example.demo.banks.data.dtos.BankDTO;
import com.example.demo.banks.data.dtos.BankDepositRequestDTO;
import com.example.demo.banks.data.dtos.BankDepositResponseDTO;
import com.example.demo.banks.data.entities.BankDepositEntity;
import com.example.demo.banks.data.entities.BankEntity;
import com.example.demo.banks.data.entities.BankOfferEntity;
import com.example.demo.banks.data.mappers.BankDepositEntityMapper;
import com.example.demo.banks.data.mappers.BankEntityMapper;
import com.example.demo.banks.data.repos.BankDepositRepo;
import com.example.demo.banks.data.repos.BankOfferRepo;
import com.example.demo.banks.data.repos.BankRepo;
import com.example.demo.users.data.repos.UserRepo;
import com.example.demo.users.exceptions.UserNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static com.example.demo.banks.data.constants.BankConstants.*;
import static com.example.demo.banks.data.factories.BankEntityFactory.buildBankEntity;
import static com.example.demo.banks.data.factories.BankOfferEntityFactory.buildBankOfferEntity;

@Service
@RequiredArgsConstructor
public class BankService {

    private final BankOfferRepo bankOfferRepo;
    private final BankRepo bankRepo;
    private final UserRepo userRepo;
    private final BankDepositRepo bankDepositRepo;
    private final BankEntityMapper bankEntityMapper;
    private final BankDepositEntityMapper bankDepositEntityMapper;

    public void addPredefinedBanksWithOffers() {
        if (bankRepo.findAll().isEmpty()) {
            addPredefinedBanks();
            addPredefinedBankOffers();
        }
    }

    private void addPredefinedBanks() {
        bankRepo.save(buildBankEntity(BT_BANK, BT_BANK_ADDRESS));
        bankRepo.save(buildBankEntity(BCR_BANK, BCR_BANK_ADDRESS));
        bankRepo.save(buildBankEntity(ING_BANK, ING_BANK_ADDRESS));
        bankRepo.save(buildBankEntity(BRD_BANK, BRD_BANK_ADDRESS));
    }

    private void addPredefinedBankOffers() {
        var bt = bankRepo.findFirstByName(BT_BANK);
        var bcr = bankRepo.findFirstByName(BCR_BANK);
        var ing = bankRepo.findFirstByName(ING_BANK);
        var brd = bankRepo.findFirstByName(BRD_BANK);

        bt.ifPresent(this::saveBTOffers);
        bcr.ifPresent(this::saveBCROffers);
        ing.ifPresent(this::saveINGOffers);
        brd.ifPresent(this::saveBRDOffers);
    }

    private void saveBTOffers(BankEntity bankEntity) {
        var offer1 = bankOfferRepo.save(buildBankOfferEntity(bankEntity, DISCOUNT_3, PERIOD_1Y));
        var offer2 = bankOfferRepo.save(buildBankOfferEntity(bankEntity, DISCOUNT_4, PERIOD_3Y));
        var offer3 = bankOfferRepo.save(buildBankOfferEntity(bankEntity, DISCOUNT_5, PERIOD_5Y));
        linkOffersToBank(bankEntity, List.of(offer1, offer2, offer3));
    }

    private void saveBCROffers(BankEntity bankEntity) {
        var offer1 = bankOfferRepo.save(buildBankOfferEntity(bankEntity, DISCOUNT_5, PERIOD_6M));
        var offer2 = bankOfferRepo.save(buildBankOfferEntity(bankEntity, DISCOUNT_8, PERIOD_3Y));
        var offer3 = bankOfferRepo.save(buildBankOfferEntity(bankEntity, DISCOUNT_12, PERIOD_5Y));
        linkOffersToBank(bankEntity, List.of(offer1, offer2, offer3));
    }

    private void saveINGOffers(BankEntity bankEntity) {
        var offer1 = bankOfferRepo.save(buildBankOfferEntity(bankEntity, DISCOUNT_4, PERIOD_6M));
        var offer2 = bankOfferRepo.save(buildBankOfferEntity(bankEntity, DISCOUNT_7, PERIOD_1Y));
        var offer3 = bankOfferRepo.save(buildBankOfferEntity(bankEntity, DISCOUNT_15, PERIOD_5Y));
        linkOffersToBank(bankEntity, List.of(offer1, offer2, offer3));
    }

    private void saveBRDOffers(BankEntity bankEntity) {
        var offer1 = bankOfferRepo.save(buildBankOfferEntity(bankEntity, DISCOUNT_3, PERIOD_6M));
        var offer2 = bankOfferRepo.save(buildBankOfferEntity(bankEntity, DISCOUNT_5, PERIOD_1Y));
        var offer3 = bankOfferRepo.save(buildBankOfferEntity(bankEntity, DISCOUNT_7, PERIOD_3Y));
        linkOffersToBank(bankEntity, List.of(offer1, offer2, offer3));
    }

    private void linkOffersToBank(BankEntity bank, List<BankOfferEntity> offers) {
        bank.setBankOffers(offers);
        bankRepo.save(bank);
    }

    public List<BankDTO> getAll() {
        return bankRepo.findAll()
                .stream()
                .map(bankEntityMapper::toDTO)
                .toList();
    }

    public Long registerOffer(BankDepositRequestDTO depositRequest, Long userId) {
        var user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        var previousDeposits = user.getDeposits();
        var currentDeposit = bankDepositEntityMapper.toEntity(depositRequest);
        currentDeposit.setUser(user);
        previousDeposits.add(currentDeposit);
        user.setDeposits(previousDeposits);
        userRepo.save(user);
        return bankDepositRepo.save(currentDeposit).getId();
    }

    public List<BankDepositResponseDTO> getDeposits(Long userId) {
        return bankDepositRepo.findAll()
                .stream()
                .filter(bankDeposit -> Objects.equals(bankDeposit.getUser().getId(), userId))
                .map((bankDeposit) -> {
                    var offer = bankOfferRepo.findFirstById(bankDeposit.getOfferId())
                            .orElseThrow(() -> new RuntimeException("No offers found for user: " + userId));

                    return BankDepositResponseDTO
                            .builder()
                            .depositId(bankDeposit.getId())
                            .createdDate(bankDeposit.getCreatedDate())
                            .amount(bankDeposit.getAmount())
                            .bankName(offer.getBank().getName())
                            .period(offer.getPeriod())
                            .discount(offer.getDiscount())
                            .build();
                })
                .toList();
    }

    @Transactional
    public Long removeDeposit(Long depositId, Long userId) {
        var user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        var deposit = bankDepositRepo.findById(depositId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        var userDeposits = user.getDeposits();
        userDeposits.remove(deposit);
        user.setDeposits(userDeposits);
        userRepo.save(user);

        return bankDepositRepo.deleteAllById(depositId);
    }

    public String generateReport(Long userId) {
        List<BankDepositEntity> deposits = getDepositsByUserId(userId);

        StringBuilder report = new StringBuilder();

        for (BankDepositEntity deposit : deposits) {
            BankOfferEntity offer = getOfferById(deposit.getOfferId());
            BankEntity bank = offer.getBank();
            Instant creationInstant = deposit.getCreatedDate().toInstant(ZoneOffset.UTC);
            LocalDate creationDate = LocalDate.ofInstant(creationInstant, ZoneId.systemDefault());
            Period offerPeriod = offer.getPeriod();
            LocalDate maturityDate = creationDate.plus(offerPeriod);
            LocalDate now = LocalDate.now();
            long remainingDays = ChronoUnit.DAYS.between(now, maturityDate);
            int finalAmount = calculateFinalAmount(deposit.getAmount(), offer.getDiscount());

            report.append("Ati bagat suma ")
                    .append(deposit.getAmount())
                    .append(" la Banca ")
                    .append(bank.getName())
                    .append(", mai aveti ")
                    .append(remainingDays)
                    .append(" zile pana la data ")
                    .append(maturityDate)
                    .append(" cand puteți primi dobanda si obtine suma finala de ")
                    .append(finalAmount)
                    .append("\n");
        }

        return report.toString();
    }

    private List<BankDepositEntity> getDepositsByUserId(Long userId) {
        return bankDepositRepo.findByUserId(userId);
    }

    private BankOfferEntity getOfferById(Long offerId) {

        return bankOfferRepo.findById(offerId).orElseThrow(() -> new RuntimeException("Offer not found"));
    }

    private int calculateFinalAmount(int initialAmount, int discount) {
        return initialAmount + (initialAmount * discount / 100);
    }
}
