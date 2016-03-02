package com.hilo.events.factory;

import com.hilo.events.interfaces.IVuEvents;

/**
 * Created by hilo on 16/3/2.
 */
public abstract class VuEventFactory {

    public abstract<T extends IVuEvents.createVus> T createVus(Class<T> clazz);

}
