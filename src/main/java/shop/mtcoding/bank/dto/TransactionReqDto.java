package shop.mtcoding.bank.dto;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.bank.config.enums.TransactionEnum;
import shop.mtcoding.bank.domain.account.Account;
import shop.mtcoding.bank.domain.transaction.Transaction;

public class TransactionReqDto {
    @Getter
    @Setter
    public static class DepositReqDto {
        private Long number;
        private Long amount;
        private String gubun;

        public Transaction toEntity(Account depositAccount) {
            Transaction transaction = Transaction.builder()
                    .withdrawAccount(null)
                    .depositAccount(depositAccount)
                    .depositAccountBalance(depositAccount.getBalance())// 입금시 마다 남는 중간잔액 체크로직
                    .amount(amount)
                    .gubun(TransactionEnum.valueOf(gubun))
                    .build();
            return transaction;
        }
    }

    @Getter
    @Setter
    public static class WithdrawReqDto {
        private Long amount;
        private String password;
        private String gubun;

        public Transaction toEntity(Account withdrawAccount) {
            Transaction transaction = Transaction.builder()
                    .withdrawAccount(withdrawAccount)
                    .depositAccount(null)
                    .withdrawAccountBalance(withdrawAccount.getBalance())// 입금시 마다 남는 중간잔액 체크로직
                    .amount(amount)
                    .gubun(TransactionEnum.valueOf(gubun))
                    .build();
            return transaction;
        }
    }
}
