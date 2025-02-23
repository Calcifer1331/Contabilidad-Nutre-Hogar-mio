package com.nutrehogar.sistemacontable.infrastructure.report;

import com.nutrehogar.sistemacontable.application.config.ConfigLoader;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class PaymentVoucher extends EntryForm {
    public PaymentVoucher() {
        super("Comprobante de Pago",
                "PaymentVoucher.jrxml",
                ConfigLoader.Props.DIR_PAYMENT_VOUCHER_NAME.getPath());
    }

}