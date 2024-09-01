package com.techlabs.app.util;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//public class BalanceDTO {
//	
//	private double totalBalance;
//    private List<AccountDTO> accounts;
//
//}


public class BalancePagedResponse<T> extends PagedResponse<T> {

    private double totalBalance;

    public BalancePagedResponse(List<T> content, int page, int size, long totalElements, int totalPages, boolean last, double totalBalance) {
        super(content, page, size, totalElements, totalPages, last);
        this.totalBalance = totalBalance;
    }
}