package jiachengyang.nyu.mydribbble;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

public class WadddleApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
