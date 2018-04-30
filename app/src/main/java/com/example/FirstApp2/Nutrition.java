package com.example.FirstApp2;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.fitness.result.DataSourcesResult;

import java.util.concurrent.TimeUnit;

public class Nutrition extends AppCompatActivity {

    private static final String TAG = "FitActivity";
    private GoogleApiClient mClient = null;
    private OnDataPointListener mListener;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);
        tv=findViewById(R.id.steps);
    }

    @Override
    protected void onResume() {
        super.onResume();
        connectFitness();
    }

    private void connectFitness() {
        if (mClient == null){
            mClient = new GoogleApiClient.Builder(this)
                    .addApi(Fitness.SENSORS_API)
                    .addScope(new Scope(Scopes.FITNESS_LOCATION_READ)) // GET STEP VALUES
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                                                @Override
                                                public void onConnected(Bundle bundle) {
                                                    Log.e(TAG, "Connected!!!");
                                                    // Now you can make calls to the Fitness APIs.
                                                    findFitnessDataSources();

                                                }

                                                @Override
                                                public void onConnectionSuspended(int i) {
                                                    // If your connection to the sensor gets lost at some point,
                                                    // you'll be able to determine the reason and react to it here.
                                                    if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
                                                        Log.i(TAG, "Connection lost.  Cause: Network Lost.");
                                                    } else if (i
                                                            == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
                                                        Log.i(TAG,
                                                                "Connection lost.  Reason: Service Disconnected");
                                                    }
                                                }
                                            }
                    )
                    .enableAutoManage(this, 0, new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult result) {
                            Log.e(TAG, "!_@@ERROR :: Google Play services connection failed. Cause: " + result.toString());
                        }
                    })
                    .build();
        }

    }

    private void findFitnessDataSources() {
        Fitness.SensorsApi.findDataSources(
                mClient,
                new DataSourcesRequest.Builder()
                        .setDataTypes(DataType.TYPE_STEP_COUNT_DELTA)
                        .setDataSourceTypes(DataSource.TYPE_DERIVED)
                        .build())
                .setResultCallback(new ResultCallback<DataSourcesResult>() {
                    @Override
                    public void onResult(DataSourcesResult dataSourcesResult) {
                        Log.e(TAG, "Result: " + dataSourcesResult.getStatus().toString());
                        for (DataSource dataSource : dataSourcesResult.getDataSources()) {
                            Log.e(TAG, "Data source found: " + dataSource.toString());
                            Log.e(TAG, "Data Source type: " + dataSource.getDataType().getName());

                            //Let's register a listener to receive Activity data!
                            if (dataSource.getDataType().equals(DataType.TYPE_STEP_COUNT_DELTA) && mListener == null) {
                                Log.i(TAG, "Data source for TYPE_STEP_COUNT_DELTA found!  Registering.");

                                registerFitnessDataListener(dataSource, DataType.TYPE_STEP_COUNT_DELTA);
                            }
                        }
                    }
                });
    }

    private void registerFitnessDataListener(final DataSource dataSource, DataType dataType) {


        // [START register_data_listener]
        mListener = new OnDataPointListener() {
            @Override
            public void onDataPoint(DataPoint dataPoint) {
                for (Field field : dataPoint.getDataType().getFields()) {
                    Value val = dataPoint.getValue(field);
                    Log.e(TAG, "Detected DataPoint field: " + field.getName());
                    Log.e(TAG, "Detected DataPoint value: " + val);
                    final int value = val.asInt();
                    if (field.getName().compareToIgnoreCase("steps") == 0) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv.setText("Steps " + value);
                            }
                        });
                    }
                }
            }
        };

        Fitness.SensorsApi.add(
                mClient,
                new SensorRequest.Builder()
                        .setDataSource(dataSource) // Optional but recommended for custom data sets.
                        .setDataType(dataType) // Can't be omitted.
                        .setSamplingRate(1, TimeUnit.SECONDS)
                        .build(),
                mListener).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                if (status.isSuccess()) {
                    Log.i(TAG, "Listener registered!");
                } else {
                    Log.i(TAG, "Listener not registered.");
                }
            }
        });

    }
}
