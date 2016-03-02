package com.hilo.events.iml;

import com.hilo.events.interfaces.IVuEvents;
import com.hilo.events.RecyclerFragmentVuEvents;
import com.hilo.events.TextVuEvents;
import com.hilo.interfaces.Vu;
import com.hilo.others.InvalidVuException;
import com.hilo.vus.RecyclerFragmentVu;
import com.hilo.vus.TextVu;

public class AllVuEventsManagerIml implements IVuEvents.createVus {

        @Override
        public IVuEvents setupVu(Vu vu) throws InvalidVuException {
            if (vu instanceof RecyclerFragmentVu) {
                return new RecyclerFragmentVuEvents((RecyclerFragmentVu) vu);
            } else if (vu instanceof TextVu) {
                return new TextVuEvents((TextVu) vu);
            } else {
                throw new InvalidVuException("No match found Vu implementation interface");
            }
        }
    }