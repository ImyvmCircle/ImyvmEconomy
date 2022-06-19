package com.imyvm.economy.interfaces;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface Serializable {
    void serialize(DataOutputStream stream) throws IOException;

    void deserialize(DataInputStream stream) throws IOException;
}
