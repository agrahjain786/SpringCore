package com.techlabs.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemCounts {
	
	private long totalActiveCustomers;
    private long totalInactiveCustomers;
    private long totalActiveAccounts;
    private long totalInactiveAccounts;
    private long totalCustomerPendingRequests;
    private long totalAccountPendingRequests;
    private long totalAdmins;
    private long totalCustomers;
    private long totalAccounts;
    private long totalCompletedRequests;

}
