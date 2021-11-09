package ru.tutazaim.webview;

import android.app.Application;

import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;

public class MyAppMetrica extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Creating an extended library configuration.
        try {
            YandexMetricaConfig config = YandexMetricaConfig.newConfigBuilder("55bd71d1-fca8-4f3b-a2fd-4887c00a6fe4").build();
            // Initializing the AppMetrica SDK.
            YandexMetrica.activate(getApplicationContext(), config);
            // Automatic tracking of user activity.
            YandexMetrica.enableActivityAutoTracking(this);
        } catch (Exception error){
        }
    }
}
