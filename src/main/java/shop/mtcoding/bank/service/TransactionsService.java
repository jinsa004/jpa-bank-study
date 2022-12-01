package shop.mtcoding.bank.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.bank.config.enums.TransactionEnum;
import shop.mtcoding.bank.config.exception.CustomApiException;
import shop.mtcoding.bank.domain.account.Account;
import shop.mtcoding.bank.domain.account.AccountRepository;
import shop.mtcoding.bank.domain.transaction.Transaction;
import shop.mtcoding.bank.domain.transaction.TransactionRepository;
import shop.mtcoding.bank.dto.TransactionReqDto.DepositReqDto;
import shop.mtcoding.bank.dto.TransactionRespDto.DepositRespDto;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class TransactionsService {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public DepositRespDto 입금하기(DepositReqDto depositReqDto) {
        // 구분값 검증(입금, 출금, 이체)
        if (TransactionEnum.valueOf(depositReqDto.getGubun()) != TransactionEnum.DEPOSIT) {
            throw new CustomApiException("구분 값 검증 실패", HttpStatus.BAD_REQUEST);
        }
        // 입금계좌 확인
        Account depositAccountPS = accountRepository.findByNumber(depositReqDto.getNumber())
                .orElseThrow(() -> new CustomApiException("해당 계좌가 없습니다.", HttpStatus.BAD_REQUEST));

        // 0원 체크
        if (depositReqDto.getAmount() <= 0) {
            throw new CustomApiException("0원은 입금될 수 없습니다.", HttpStatus.BAD_REQUEST);
        }
        // 실행 (핵심로직) update와 save 2개가 같이 일어남. 트랜잭션 관리
        depositAccountPS.입금하기(depositReqDto.getAmount()); // 더티체킹 (update) 바뀐 상태를 확인하고 자동으로 수정
        Transaction transaction = depositReqDto.toEntity(depositAccountPS); // 히스토리 세이브
        Transaction transactionPS = transactionRepository.save(transaction);

        // DTO 리턴 (정해진 값이 아니라 서비스를 어떻게 설계할지에 따라 다르기때문에 생각해봐야함)
        return new DepositRespDto(transactionPS);
    }

}
