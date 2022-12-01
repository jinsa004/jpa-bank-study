package shop.mtcoding.bank.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.bank.domain.transaction.Transaction;

public class TransactionRespDto {
    @Getter
    @Setter
    public static class DepositRespDto {
        private Long id;
        private Long amount;
        private String gubun;
        private String from;
        private String to;
        @JsonIgnore // 컨트롤러에서는 json이라 안나오지만 서비스에선 json으로 파싱 변경전이라 서비스 단에서 확인가능해짐
        private Long depositAccountBalance; // 테스트 코드를 위한 데이터

        public DepositRespDto(Transaction transaction) {
            this.id = transaction.getId();
            this.amount = transaction.getAmount();
            this.gubun = transaction.getGubun().getValue();// 입금
            this.from = "ATM";
            this.to = transaction.getDepositAccount().getNumber() + ""; // 숫자를 문자열로 바꾸기 위해 "" 삽입
            this.depositAccountBalance = transaction.getDepositAccountBalance(); // 테스트 코드 확인을 위한 데이터
        }

    }
}
