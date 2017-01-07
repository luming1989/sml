package org.hw.sml.support.rmi;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteInvoke extends Remote{
	
	public Serializable invoke(Serializable info)  throws RemoteException;
	
}
