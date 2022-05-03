package myplugin;

import org.datanucleus.cache.SoftRefCache;
import org.datanucleus.cache.WeakRefCache;
import org.datanucleus.util.NucleusLogger;

//BUG? we get many "L1 Cache op IS NULL" in the log with this
//public class NoClearLevel1Cache extends WeakRefCache {
public class NoClearLevel1Cache extends SoftRefCache {
//	@Override
//	public ObjectProvider<T> remove(Object id) {}
	
	public NoClearLevel1Cache() {
		NucleusLogger.GENERAL.info(">> NoClearLevel1Cache ctor() was called.");
	}
	
	@Override
	public void clear() {
		NucleusLogger.GENERAL.info(">> NoClearLevel1Cache clear() was called.");
	}
}
