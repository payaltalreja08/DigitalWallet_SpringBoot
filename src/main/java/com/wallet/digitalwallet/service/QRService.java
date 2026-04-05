package com.wallet.digitalwallet.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.awt.image.BufferedImage;

@Service
public class QRService {
    private static final Logger log = LoggerFactory.getLogger(QRService.class);

    public BufferedImage generateQRCode(String content) throws Exception {
        log.debug("Tracing: Generating QR code with content: {}", content);
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = barcodeWriter.encode(content, BarcodeFormat.QR_CODE, 200, 200);
        log.info("Successfully generated QR code");
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
}
