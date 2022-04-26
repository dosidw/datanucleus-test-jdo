package myplugin;

import org.datanucleus.cache.SoftRefCache;
import org.datanucleus.cache.WeakRefCache;

//BUG? we get many "L1 Cache op IS NULL" in the log with this
//public class NoClearLevel1Cache extends WeakRefCache {
public class NoClearLevel1Cache extends SoftRefCache {
//	@Override
//	public ObjectProvider<T> remove(Object id) {}
	@Override
	public void clear() {}
}
