/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 *
 * @author newbiecihuy
 */
@Configuration
public class TransactionManagerConfig {

    @Bean(name = Constants.TRANSACTION_MANAGER_CHAINED)
    public ChainedTransactionManager chainedTransactionManager(
            @Qualifier(Constants.TRANSACTION_MANAGER_LF) PlatformTransactionManager transactionManagerLf) {
        return new ChainedTransactionManager(transactionManagerLf);
    }

}
