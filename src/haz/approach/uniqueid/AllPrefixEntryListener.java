package haz.approach.uniqueid;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;
import com.hazelcast.core.MapEvent;

public class AllPrefixEntryListener<K,V> implements EntryListener {
	UniqueIDDBUtility dbUtil = new UniqueIDDBUtility();
	@Override
	public void entryAdded(EntryEvent ee) {
		String ins = (String) ee.getKey();
		Long val = (Long) ee.getValue()+100; 
		try {
			dbUtil.insertRow(ins,val);
		} catch(SQLIntegrityConstraintViolationException se){
			try {
				dbUtil.updateRow(ins,val);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		catch (SQLException e) {
			System.out.println("exception - "+e);
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void entryEvicted(EntryEvent arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void entryRemoved(EntryEvent arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void entryUpdated(EntryEvent ee) {
		
	/*	if((Long)ee.getValue() % 100==0 && (Long)ee.getValue() % 200!=0)
		{
			String ins = (String) ee.getKey();
			Long val = (Long) ee.getValue()+100; 
			try {
				dbUtil.updateRow(ins,val);
			} catch (SQLException e) {
				e.printStackTrace();
			}
	     }
	     */
	}
		

	@Override
	public void mapCleared(MapEvent arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void mapEvicted(MapEvent arg0) {
		throw new UnsupportedOperationException();
	}

}
