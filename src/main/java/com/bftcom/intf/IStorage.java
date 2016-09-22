package com.bftcom.intf;

import com.bftcom.ws.config.Configurator;
import com.bftcom.ws.objmodel.Messenger;

/**
 * Created by d.dyldaev on 18.09.16.
 */
public interface IStorage {

  void saveData(Messenger msgr);

  void readData(Messenger msgr);

  void init(Configurator.Config cfg);
}
