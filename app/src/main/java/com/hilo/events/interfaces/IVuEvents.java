package com.hilo.events.interfaces;

import com.hilo.interfaces.Vu;
import com.hilo.others.InvalidVuException;

/**
 * Created by hilo on 16/3/2.
 */
public abstract class IVuEvents {

    public interface createVus {
        IVuEvents setupVu(Vu vu) throws InvalidVuException;
    }
}
