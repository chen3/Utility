package cn.qiditu.utility;

import android.os.Handler;
import android.support.annotation.NonNull;

import cn.qiditu.property.ReadProperty;
import cn.qiditu.property.ReadWriteProperty;
import cn.qiditu.signalslot.signals.Signal0;
import cn.qiditu.signalslot.slots.Slot0;

@SuppressWarnings({"WeakerAccess", "unused"})
public class Timer {

    /**
     * This signal is emitted when the timer times out.
     * <p>
     *     Note: This is a private signal. It can be used in signal connections but cannot be
     *     emitted by the user.
     * </p>
     */
    @SuppressWarnings("WeakerAccess")
    public final Signal0 timeOut = new Signal0(this);

    /**
     * This property holds the timeout interval in milliseconds.
     * The default value for this property is 0.
     */
    @SuppressWarnings("WeakerAccess")
    public final ReadWriteProperty<Integer> interval = new ReadWriteProperty<>(0);

    /**
     * This boolean property is true if the timer is running; otherwise false.
     * The default value for this property is false.
     */
    private final ReadWriteProperty<Boolean> active = new ReadWriteProperty<>(false);
    @SuppressWarnings("unused")
    @NonNull
    public ReadProperty<Boolean> active() {
        return active;
    }

    /**
     * This property holds whether the timer is a single-shot timer.
     * A single-shot timer fires only once, non-single-shot timers fire every interval milliseconds.
     * The default value for this property is false.
     */
    @SuppressWarnings("WeakerAccess")
    public final ReadWriteProperty<Boolean> singleShot = new ReadWriteProperty<>(false);

    /**
     * This static function calls a slot after a given time interval.
     *
     * It is very convenient to use this function because you do not need to bother with
     * create a local Timer object.
     * @param msec The time interval is msec milliseconds.
     * @param slot The slot will work after time out.
     */
    @SuppressWarnings("unused")
    public static void singleShot(int msec, @NonNull Slot0 slot) {
        Timer timer = new Timer();
        timer.interval.set(msec);
        timer.singleShot.set(true);
        timer.timeOut.connect(slot);
        timer.start();
    }

    /**
     * This static function calls a slot after a given time interval.
     *
     * It is very convenient to use this function because you do not need to bother with
     * create a local Timer object.
     * @param msec The time interval is msec milliseconds.
     * @param signal The signal will emit after time out.
     */
    @SuppressWarnings("unused")
    public static void singleShot(int msec, @NonNull Signal0 signal) {
        Timer timer = new Timer();
        timer.interval.set(msec);
        timer.singleShot.set(true);
        timer.timeOut.connect(signal);
        timer.start();
    }

    /**
     * This function overloads start(int)
     */
    @SuppressWarnings("WeakerAccess")
    public void start() {
        final Boolean value = active.get();
        if(value == null ? false : value) {
            stop();
        }
        final Integer integer = interval.get();
        if(handler.postDelayed(runnable, integer == null ? 0 : integer)) {
            active.set(true);
        }
    }

    /**
     * Starts or restarts the timer with a timeout interval of msec milliseconds.
     * If the timer is already running, it will be stopped and restarted.
     * If singleShot is true, the timer will be activated only once.
     * @param msec The time interval is msec milliseconds.
     */
    @SuppressWarnings("unused")
    public void start(int msec) {
        interval.set(msec);
        start();
    }

    /**
     * Stops the timer.
     */
    @SuppressWarnings("WeakerAccess")
    public void stop() {
        Boolean value = active.get();
        if(value == null ? false : value) {
            handler.removeCallbacks(runnable);
        }
    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        /**
         * {@inheritDoc}
         */
        @Override
        public void run() {
            final Boolean value = singleShot.get();
            if(value == null ? false : value) {
                active.set(false);
            }
            else {
                final Integer integer = interval.get();
                handler.postDelayed(runnable, integer == null ? 0 : integer);
            }
            Timer.this.timeOut.emit();
        }
    };

}
