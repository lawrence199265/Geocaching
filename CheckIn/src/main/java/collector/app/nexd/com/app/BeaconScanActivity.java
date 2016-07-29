package collector.app.nexd.com.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

import cn.nexd.sdk.collector.NexdCollectorAgent;
import cn.nexd.sdk.collector.NexdCollectorConfiguration;
import cn.nexd.sdk.collector.core.CollectorResultListener;
import collector.app.nexd.com.app.model.TargetModel;

public class BeaconScanActivity extends AppCompatActivity  implements CollectorResultListener{
    private NexdCollectorConfiguration.CollectionMode collectorMode;
    private NexdCollectorConfiguration.Buidler collectorConfig;
    private NexdCollectorAgent collectorAgent;
    private long beaconCollectorDelay=10000L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_scan);
        initCollectConfig();
    }

    private void initCollectConfig() {
        collectorMode = NexdCollectorConfiguration.CollectionMode.COLLECTION_MODE_BEACON_ONLY;
        collectorConfig = new NexdCollectorConfiguration.Buidler();
        collectorConfig.setBeaconCollectorEnable(true);
        collectorConfig.setBeaconCollectorDelay(beaconCollectorDelay);



    }


    @Override
    public void onCollectorChanged(int i, List list) {

    }
}
