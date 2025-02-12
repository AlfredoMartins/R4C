package ao.co.r4c.helper;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import ao.co.r4c.R;

public class QRCodeActivity extends AppCompatActivity {

    Button btn_qr_code;
    ImageView img_qr_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        btn_qr_code = findViewById(R.id.btn_scan);
        img_qr_code = findViewById(R.id.img_qr_code);


        btn_qr_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gerarCodigoQR("Alfredo");
            }
        });
    }

    private void gerarCodigoQR(String string_code) {
        if (string_code != null) {
            try {
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                BitMatrix bitMatrix = multiFormatWriter.encode(string_code, BarcodeFormat.QR_CODE, 500, 500);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

                img_qr_code.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
