package mobileapp.jianhuang.assign_3;

/**
 * Created by jianhuang on 16-01-31.
 */
public interface AccelerometerListener {

    public void onAccelerationChanged(float x, float y, float z);

    public void onShake(float force);

}
