package com.hilo.events.factory;

import com.hilo.events.interfaces.IVuEvents;
import com.hilo.utils.LogUtils;

/**
 * Created by hilo on 16/3/2.
 */
public class CreateVuSubClass extends VuEventFactory {

    @Override
    public <T extends IVuEvents.createVus> T createVus(Class<T> clazz) {
        T event;
        try {
            event = (T) Class.forName(clazz.getName()).newInstance();
            LogUtils.I("hilo", event + "");
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return event;
    }
}
