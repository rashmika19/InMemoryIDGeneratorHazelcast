package haz.approach.uniqueid;

import java.sql.SQLException;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IAtomicLong;
import com.hazelcast.core.IFunction;
import com.hazelcast.core.IMap;

public class HZUniqueIDGenerator {
	static HazelcastInstance hazelcastInstance = null;
	static IMap<String, Long> allPrefixes = null;
	
	static{
		hazelcastInstance = Hazelcast.newHazelcastInstance();
		allPrefixes = hazelcastInstance.getMap( "allPrefixes" );
		allPrefixes.addEntryListener(new AllPrefixEntryListener<String, Long>(), true);
	}
	
	public static Long generateID(final String prefix){
		UniqueIDDBUtility dbUtil = new UniqueIDDBUtility();
		final IAtomicLong seqIdLong = hazelcastInstance.getAtomicLong(prefix);
		seqIdLong.alter(new  IFunction<Long,Long>(){
			
			@Override
			public Long apply(Long input) {
				if(input == null || input == 0){
					try {
						Long currVal = dbUtil.selectRow(prefix);
						if(currVal == null) return input;
						return dbUtil.selectRow(prefix);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					return input;      
				}
				return input;   
			}
			
		});
	
		Long seqId = seqIdLong.incrementAndGet();
		if (seqId % 100 == 0)
		{
			try {
			dbUtil.updateRow(prefix,seqId+100);
			} catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		allPrefixes.put( prefix, seqId );
		return seqId;
	}
	
}

