package uniks.de.iotexample;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = findViewById(R.id.viewPager);
        mViewPager.setAdapter(new ServicePagerAdapter(getSupportFragmentManager()));


        FloatingActionButton asb = findViewById(R.id.addServiceButton);

        asb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IntentIntegrator(MainActivity.this).initiateScan();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (intentResult !=null){
            String barcode = intentResult.getContents();

            Log.d(getClass().getSimpleName(),"scanned barcode: " + barcode);

            Toast.makeText(this,"scanned barcode: " + barcode,Toast.LENGTH_LONG).show();

            if (URLUtil.isValidUrl(barcode)){
                ServicePagerAdapter adapter = (ServicePagerAdapter) mViewPager.getAdapter();

                adapter.addService(barcode);


            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
}
